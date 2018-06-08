# -*- coding: utf-8 -*-
from __future__ import unicode_literals
from rest_framework.views import APIView
from rest_framework.parsers import JSONParser
from django.http import JsonResponse
from django.utils.six import BytesIO
from models import AppInfo, Bank, BranchDetail, State, District, City, BranchDetail
from serializers import ApiInfoSerializer, BankSerializer, BranchDetailSerializer, BankDetailSerializer

import logging


class ApiInfo(APIView):
    def get(self, req):
        apiInfo = AppInfo(info="This BNIFSC API.")
        serializer = ApiInfoSerializer(apiInfo)
        return JsonResponse(serializer.data)


class BankApi(APIView):
    def get(self, req):
        banks = Bank.objects.all()
        serializer = BankSerializer(banks, many=True)
        return JsonResponse(serializer.data, safe=False)


class StateApi(APIView):
    def get(self, request):
        logging.info("Request bank name %s", request.GET["bank_id"])
        bank = Bank.objects.get(bank_id=request.GET["bank_id"])
        logging.info("Bank database name = %s", bank.name)
        bank_details = BranchDetail.objects.filter(bank_id=bank)
        serializer = BranchDetailSerializer(bank_details, many=True)
        return JsonResponse(serializer.data, safe=False)


class BankDetailApi(APIView):
    def post(self, request):
        logging.info("Request Data %s", request.data)
        stream = BytesIO(request.data['_content'])
        data = JSONParser().parse(stream=stream)
        logging.info("Data %s", data)
        serializer = BankDetailSerializer(data=data)
        if serializer.is_valid():
            logging.info("Valid Data")
            validated_data = serializer.validated_data
            
            # Bank section
            bank = Bank().get_by_name(validated_data["bank"])
            if bank is None:                
                bank = Bank(name=validated_data["bank"], url_name=validated_data["bank"])
                bank.save()
            
            # State section 
            state = State().by_bank_and_state(bank, validated_data["state"])
            if state is None:                
                state = State(
                    name=validated_data["state"], url_name=validated_data["state"])
                state.bank.add(bank)
                state.save()
            
            # District section 
            district = District().by_state_and_district(state,validated_data['district'])
            if district is None:
                district = District(name=validated_data['district'],url_name=validated_data['district'],state=state)
                district.save()
            
            # City Section 
            city = City().by_district_and_city(district,validated_data['city'])
            if city is None:
                city = City(name=validated_data['city'],url_name=validated_data['city'],district = district)
                city.save()
            # BranchDetail Section
            branch = BranchDetail().by_ifsc(validated_data['ifsc'])
            if branch is None:
                branch = BranchDetail(bank = bank,branch_name=validated_data['branch'],branch_url_name=validated_data['branch'],ifsc_code=validated_data['ifsc'],micr=validated_data['micr'],land_line=validated_data['contact'],state=state,district=district,city=city,address=validated_data['address'])
                branch.save()
            branch_detail_serializer = BranchDetailSerializer(branch)    
            return JsonResponse(branch_detail_serializer.data)
        else:
            logging.warning("Not a valid data")
            return JsonResponse(serializer.errors)
