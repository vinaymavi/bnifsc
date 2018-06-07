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
            logging.warning("valid data")
            validated_data = serializer.validated_data
            bank = Bank().get_by_name(validated_data["bank"])
            if bank is None:
                logging.info("Inside if block")
                bank = Bank(name=validated_data["bank"], url_name="vinay")
                bank.save()
            state = State().by_bank_and_state(bank, validated_data["state"])
            if state is None:
                logging.info("State if block")
                state = State(
                    name=validated_data["state"], url_name=validated_data["state"])
                state.bank.add(bank)
                state.save()
            return JsonResponse(serializer.validated_data)
        else:
            logging.warning("Not a valid data")
            return JsonResponse(serializer.errors)
