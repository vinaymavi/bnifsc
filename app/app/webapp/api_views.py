# -*- coding: utf-8 -*-
from __future__ import unicode_literals
from django import views
from django.http import JsonResponse
from models import AppInfo, Bank
from serializers import ApiInfoSerializer, BankSerializer


class ApiInfo(views.View):
    def get(self, req):
        apiInfo = AppInfo(info="This BNIFSC API.")
        serializer = ApiInfoSerializer(apiInfo)
        return JsonResponse(serializer.data)


class BankApi(views.View):
    def get(self, req):
        banks = Bank.objects.all()
        serializer = BankSerializer(banks, many=True)
        return JsonResponse(serializer.data, safe=False)
