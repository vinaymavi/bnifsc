# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render
from django.http import HttpResponse
from models import *
import logging
from django.conf import settings
from serializers import *


def index(request):
    banks = Bank().list_all()
    page = Page.by_page_name(settings.URL_PAGE_MAPPING['HOME_PAGE'])
    page_serializer = PageSerializer(page)
    bank_serializer = BankSerializer(banks, many=True)
    context = {"banks": bank_serializer.data,
               'seo_data': page_serializer.data,
                "page_items": bank_serializer.data
                }
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


def bank(request, seo_string, bank_id):
    params = {'bank_id': int(bank_id)}
    banks = Bank().list_all()
    bank_serializer = BankSerializer(banks, many=True)
    bank = Bank.objects.get(bank_id=bank_id)
    logging.info("Bank database name = %s and id=%s", bank.name, bank.pk)
    state_list = State().by_bank_id(bank.pk)
    state_serializer = StateSerializer(state_list, many=True, context={'bank_id':bank_id,'bank_name':bank.name})
    page = Page.by_page_name(settings.URL_PAGE_MAPPING['BANK_PAGE'])
    page_serializer = PageSerializer(page)
    context = {
        'params': params,
        'banks': bank_serializer.data,
        'states': state_serializer.data,
        'page_items': state_serializer.data,
        'seo_data': page_serializer.data,
    }
    return render(request, 'bank_page.html', context=context)


def state(request, seo_string, bank_id, state_id):
    params = {'bank_id': int(bank_id),'state_id':int(state_id)}
    
    banks = Bank().list_all()
    bank_serializer = BankSerializer(banks, many=True)    
    bank = Bank.objects.get(bank_id=bank_id)
    logging.info("Bank database name = %s and id=%s", bank.name, bank.pk)

    state_list = State().by_bank_id(bank.pk)
    state_serializer = StateSerializer(state_list, many=True, context={'bank_id':bank_id,'bank_name':bank.name})    
    state = State().by_state_id(state_id)

    districts = District().by_bank_and_state(bank,state)
    district_serializer = DistrictSerializer(
        districts, 
        many=True,
        context={'bank_id':bank_id,'bank_name':bank.name,'state_id':state_id,'state_name':state.name}
        )
    page = Page.by_page_name(settings.URL_PAGE_MAPPING['STATE_PAGE'])
    page_serializer = PageSerializer(page)
    context = {
        'params': params,
        'banks': bank_serializer.data,
        'states': state_serializer.data,
        'districts': district_serializer.data,
        'page_items': district_serializer.data,
        'seo_data': page_serializer.data,
    }
    return render(request, 'bank_page.html', context=context)


def district(request, seo_string, bank_id, state_id, district_id):
    return HttpResponse("Bank id {},state id {},district id {}".format(bank_id, state_id, district_id))


def city(request, seo_string, bank_id, state_id, district_id, city_id):
    return HttpResponse("Bank id {},state id {},district id {},city id {}".format(bank_id, state_id, district_id, city_id))


def branch(request, seo_string, bank_id, state_id, district_id, city_id, branch_id):
    return HttpResponse("Bank id {},state id {},district id {},city id {},branch id {}".format(bank_id, state_id, district_id, city_id, branch_id))
