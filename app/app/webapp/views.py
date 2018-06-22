# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render
from django.http import HttpResponse
from models import Bank, Page
import logging
from django.conf import settings
from serializers import PageSerializer,BankSerializer


def index(request):
    banks = Bank().list_all()
    page = Page.by_page_name(settings.URL_PAGE_MAPPING['HOME_PAGE'])
    page_serializer = PageSerializer(page)
    bank_serializer = BankSerializer(banks, many=True)
    context = {"banks": bank_serializer.data, 'seo_data': page_serializer.data}
    logging.info("Banks count = %s" % (len(context["banks"])))
    return render(request, 'basic_page.html', context=context)


def aboutus(request):
    page = Page.by_page_name(settings.URL_PAGE_MAPPING['ABOUTUS_PAGE'])
    serializer = PageSerializer(page)
    context = {'seo_data': serializer.data}
    return render(request, 'aboutus.html', context=context)


def disclaimer(request):
    page = Page.by_page_name(settings.URL_PAGE_MAPPING['DISCLAIMER_PAGE'])
    serializer = PageSerializer(page)
    context = {'seo_data': serializer.data}
    return render(request, 'disclaimer.html', context=context)


def privacy(request):
    page = Page.by_page_name(settings.URL_PAGE_MAPPING['PRIVACY_PAGE'])
    serializer = PageSerializer(page)
    context = {'seo_data': serializer.data}
    return render(request, 'privacy.html', context=context)


def contactus(request):
    page = Page.by_page_name(settings.URL_PAGE_MAPPING['CONTACTUS_PAGE'])
    serializer = PageSerializer(page)
    context = {'seo_data': serializer.data}
    return render(request, 'contactus.html', context=context)


def by_ifsc(request):
    page = Page.by_page_name(settings.URL_PAGE_MAPPING['BY_IFSC_PAGE'])
    serializer = PageSerializer(page)
    context = {'seo_data': serializer.data}
    return render(request, 'by_ifsc.html', context=context)

def bank(request,seo_string, bank_id):
    return HttpResponse("Bank id {}".format(bank_id))

def state(request,seo_string, bank_id,state_id):
    return HttpResponse("Bank id {},state id {}".format(bank_id,state_id))

def district(request,seo_string, bank_id,state_id,district_id):
    return HttpResponse("Bank id {},state id {},district id {}".format(bank_id,state_id,district_id))

def city(request,seo_string, bank_id,state_id,district_id,city_id):
    return HttpResponse("Bank id {},state id {},district id {},city id {}".format(bank_id,state_id,district_id,city_id))    

def branch(request,seo_string, bank_id,state_id,district_id,city_id,branch_id):
    return HttpResponse("Bank id {},state id {},district id {},city id {},branch id {}".format(bank_id,state_id,district_id,city_id,branch_id))
