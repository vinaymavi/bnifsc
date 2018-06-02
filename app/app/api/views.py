# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render
from django.http import JsonResponse
from serializer import InfoSerializer
from models import ApiInfo


# Create your views here.
def index(request):
    info = ApiInfo(info="BNIFSC api ready.")
    infoSrl = InfoSerializer(info)
    return JsonResponse(infoSrl.data)
