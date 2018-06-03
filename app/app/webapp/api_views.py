# -*- coding: utf-8 -*-
from __future__ import unicode_literals
from rest_framework.views import APIView
from django.http import JsonResponse
from models import AppInfo, Bank, BranchDetail
from serializers import ApiInfoSerializer, BankSerializer, BankDetailSerializer

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
        serializer = BankDetailSerializer(bank_details, many=True)
        return JsonResponse(serializer.data, safe=False)
