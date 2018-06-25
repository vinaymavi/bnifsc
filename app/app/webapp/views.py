# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render,redirect
from django.http import HttpResponse,HttpResponseRedirect
from models import *
import logging
from django.conf import settings
from serializers import *
import utils
from google.appengine.api import memcache
from django.template import loader, Context

def index(request):
    path = request.path + request.GET.urlencode(safe=True)
    logging.info(path)
    # memcache implementation
    output_str = memcache.get(path)
    if output_str is not None:
        return HttpResponse(output_str)

    banks = Bank().list_all()
    page = Page.by_page_name(settings.URL_PAGE_MAPPING['HOME_PAGE'])
    page_serializer = PageSerializer(page)
    bank_serializer = BankSerializer(banks, many=True)
    seo_context={        
    }

    context = {"banks": bank_serializer.data,
               'seo_data': utils.process_seo_data(page_serializer.data,seo_context),
                "page_items": bank_serializer.data
                }
    logging.info("Banks count = %s" % (len(context["banks"])))
    template = loader.get_template('basic_page.html')    
    output_str = template.render(context)
    path = request.path
    memcache.add(path,output_str,time=24*3600)
    return HttpResponse(output_str)


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
    error = request.GET['error'] if 'error' in request.GET else None
    ifsc_code = request.GET['ifsc_code'] if 'ifsc_code' in request.GET else None

    page = Page.by_page_name(settings.URL_PAGE_MAPPING['BY_IFSC_PAGE'])
    serializer = PageSerializer(page)
    context = {'seo_data': serializer.data}    
    if error:
        context['error']={
            "msg":"Not a valid <strong>{}</strong> ifsc code.".format(ifsc_code)
        }
    return render(request, 'by_ifsc.html', context=context)


def bank(request, seo_string, bank_id):
    path = request.path + request.GET.urlencode(safe=True)
    # memcache implementation
    output_str = memcache.get(path)
    if output_str is not None:
        return HttpResponse(output_str)

    params = {'bank_id': int(bank_id)}
    banks = Bank().list_all()
    bank_serializer = BankSerializer(banks, many=True)
    bank = Bank.objects.get(bank_id=bank_id)
    logging.info("Bank database name = %s and id=%s", bank.name, bank.pk)
    state_list = State().by_bank_id(bank.pk)
    state_serializer = StateSerializer(state_list, many=True, context={'bank_id':bank_id,'bank_name':bank.name})
    page = Page.by_page_name(settings.URL_PAGE_MAPPING['BANK_PAGE'])
    page_serializer = PageSerializer(page)
    seo_context = {
        'bank_name':bank.name
    }
    context = {
        'params': params,
        'banks': bank_serializer.data,
        'states': state_serializer.data,
        'page_items': state_serializer.data,
        'seo_data': utils.process_seo_data(page_serializer.data,seo_context),
    }
    template = loader.get_template('bank_page.html')
    output_str = template.render(context)
    path = request.path
    memcache.add(path,output_str,time=24*3600)
    return HttpResponse(output_str)


def state(request, seo_string, bank_id, state_id):
    path = request.path + request.GET.urlencode(safe=True)
    # memcache implementation
    output_str = memcache.get(path)
    if output_str is not None:
        return HttpResponse(output_str)
        
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
    seo_context={
        'bank_name':bank.name,
        'state_name':state.name
    }
    context = {
        'params': params,
        'banks': bank_serializer.data,
        'states': state_serializer.data,
        'districts': district_serializer.data,
        'page_items': district_serializer.data,
        'seo_data': utils.process_seo_data(page_serializer.data,seo_context),
    }
    template = loader.get_template('bank_page.html')
    output_str = template.render(context)
    path = request.path
    memcache.add(path,output_str,time=24*3600)
    return HttpResponse(output_str)
    


def district(request, seo_string, bank_id, state_id, district_id):
    path = request.path + request.GET.urlencode(safe=True)
    # memcache implementation
    output_str = memcache.get(path)
    if output_str is not None:
        return HttpResponse(output_str)

    params = {'bank_id': int(bank_id),'state_id':int(state_id),'district_id':int(district_id)}
    
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
    district = District().by_district_id(district_id)


    cities = City().by_bank_and_district(bank,district)
    city_serializer = CitySerializer(
        cities, 
        many=True,
        context={'bank_id':bank_id,'bank_name':bank.name,'state_id':state_id,'state_name':state.name,'district_id':district_id,'district_name':district.name}
        )

    page = Page.by_page_name(settings.URL_PAGE_MAPPING['DISTRICT_PAGE'])
    page_serializer = PageSerializer(page)
    seo_context = {
        'bank_name':bank.name,
        'state_name':state.name,
        'district_name':district.name
    }
    context = {
        'params': params,
        'banks': bank_serializer.data,
        'states': state_serializer.data,
        'districts': district_serializer.data,
        'cities':city_serializer.data,
        'page_items': city_serializer.data,
        'seo_data': utils.process_seo_data(page_serializer.data,seo_context),
    }
    template = loader.get_template('bank_page.html')
    output_str = template.render(context)
    path = request.path
    memcache.add(path,output_str,time=24*3600)
    return HttpResponse(output_str)
    


def city(request, seo_string, bank_id, state_id, district_id, city_id):
    path = request.path + request.GET.urlencode(safe=True)
    # memcache implementation
    output_str = memcache.get(path)
    if output_str is not None:
        return HttpResponse(output_str)
    params = {'bank_id': int(bank_id),'state_id':int(state_id),'district_id':int(district_id),'city_id':int(city_id)}
    
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
    district = District().by_district_id(district_id)


    cities = City().by_bank_and_district(bank,district)
    city_serializer = CitySerializer(
        cities, 
        many=True,
        context={'bank_id':bank_id,'bank_name':bank.name,'state_id':state_id,'state_name':state.name,'district_id':district_id,'district_name':district.name}
        )
    city = City().by_city_id(city_id)

    branches = BranchDetail().by_bank_and_city(bank, city)
    branch_serializer = BranchDetailSerializer(
        branches, 
        many=True,
        context={'bank_id':bank_id,'bank_name':bank.name,'state_id':state_id,'state_name':state.name,'district_id':district_id,'district_name':district.name,'city_id':city_id,'city_name':city.name}
        )

    page = Page.by_page_name(settings.URL_PAGE_MAPPING['CITY_PAGE'])
    page_serializer = PageSerializer(page)
    seo_context = {
        'bank_name':bank.name,
        'state_name':state.name,
        'district_name':district.name,
        'city_name':city.name
    }
    context = {
        'params': params,
        'banks': bank_serializer.data,
        'states': state_serializer.data,
        'districts': district_serializer.data,
        'cities':city_serializer.data,
        'branches':branch_serializer.data,
        'page_items': branch_serializer.data,        
        'seo_data': utils.process_seo_data(page_serializer.data,seo_context),
    }
    template = loader.get_template('bank_page.html')
    output_str = template.render(context)
    path = request.path
    memcache.add(path,output_str,time=24*3600)
    return HttpResponse(output_str)


def branch(request, seo_string, bank_id, state_id, district_id, city_id, branch_id):
    path = request.path + request.GET.urlencode(safe=True)
    # memcache implementation
    output_str = memcache.get(path)
    if output_str is not None:
        return HttpResponse(output_str)
    params = {'bank_id': int(bank_id),'state_id':int(state_id),'district_id':int(district_id),'city_id':int(city_id),'branch_id':int(branch_id)}
    
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
    district = District().by_district_id(district_id)


    cities = City().by_bank_and_district(bank,district)
    city_serializer = CitySerializer(
        cities, 
        many=True,
        context={'bank_id':bank_id,'bank_name':bank.name,'state_id':state_id,'state_name':state.name,'district_id':district_id,'district_name':district.name}
        )
    city = City().by_city_id(city_id)

    branches = BranchDetail().by_bank_and_city(bank, city)
    branch_serializer = BranchDetailSerializer(
        branches, 
        many=True,
        context={'bank_id':bank_id,'bank_name':bank.name,'state_id':state_id,'state_name':state.name,'district_id':district_id,'district_name':district.name,'city_id':city_id,'city_name':city.name}
        )
    branch = BranchDetail().by_branch_id(branch_id)
    serializer = BranchDetailSerializer(
        branch,         
        context={'bank_id':bank_id,'bank_name':bank.name,'state_id':state_id,'state_name':state.name,'district_id':district_id,'district_name':district.name,'city_id':city_id,'city_name':city.name}
        )
    

    page = Page.by_page_name(settings.URL_PAGE_MAPPING['BRANCH_PAGE'])
    page_serializer = PageSerializer(page)
    seo_context = {
        'bank_name':bank.name,
        'state_name':state.name,
        'district_name':district.name,
        'city_name':city.name,
        'branch_name':branch.name
    }
    context = {
        'params': params,
        'banks': bank_serializer.data,
        'states': state_serializer.data,
        'districts': district_serializer.data,
        'cities':city_serializer.data,
        'branches':branch_serializer.data,
        'branch': serializer.data,
        'seo_data': utils.process_seo_data(page_serializer.data, seo_context)
    }
    template = loader.get_template('branch_page.html')
    output_str = template.render(context)
    path = request.path
    memcache.add(path,output_str,time=24*3600)
    return HttpResponse(output_str)
    

def validate_ifsc(request):
    ifsc_code = request.GET['ifsc_code']
    branch = BranchDetail().by_ifsc(ifsc_code.strip())

    if branch is not None:
        return HttpResponseRedirect(reverse('ifsc_code_info', args=['ifsc-code',ifsc_code]))
    else:
        return HttpResponseRedirect('%s?error=1&ifsc_code=%s' % (reverse('by_ifsc'),ifsc_code))




def ifsc_code_info(request,seo_string,ifsc_code):
    path = request.path + request.GET.urlencode(safe=True)
    # memcache implementation
    output_str = memcache.get(path)
    if output_str is not None:
        return HttpResponse(output_str)
    branch = BranchDetail().by_ifsc(ifsc_code.strip())
    branch_serializer = BranchDetailSerializer(
        branch,
        context={'bank_id':1,'bank_name':"bank name",'state_id':2,'state_name':"state name",'district_id':3,'district_name':"district name",'city_id':4,'city_name':"city name"}
        )
    branch_serializer = BranchDetailSerializer(
        branch,
        context={
        'bank_id':branch_serializer.data['bank']['bank_id'],
        'bank_name':branch_serializer.data['bank']['name'],
        'state_id':branch_serializer.data['state']['state_id'],
        'state_name':branch_serializer.data['state']['name'],
        'district_id':branch_serializer.data['district']['district_id'],
        'district_name':branch_serializer.data['district']['name'],
        'city_id':branch_serializer.data['city']['city_id'],
        'city_name':branch_serializer.data['city']['name']
        }
        )
    page = Page.by_page_name(settings.URL_PAGE_MAPPING['IFSC_CODE_INFO'])
    page_serializer = PageSerializer(page)
    logging.info(branch_serializer.data)
    seo_context = {
        'bank_name':branch_serializer.data['bank']['name'],
        'state_name':branch_serializer.data['state']['name'],
        'district_name':branch_serializer.data['district']['name'],
        'city_name':branch_serializer.data['city']['name'],
        'branch_name':branch.name
    }
    logging.info(seo_context)
    context = {
        'seo_data': utils.process_seo_data(page_serializer.data,seo_context),
        'branch': branch_serializer.data,
        'message':"Valid ifsc code <strong>{}</strong>.".format(branch.ifsc_code)
        }
    template = loader.get_template('ifsc_code_info.html')
    output_str = template.render(context)
    path = request.path
    memcache.add(path,output_str,time=24*3600)
    return HttpResponse(output_str)
    

def robots(request):
    output_string = 'User-agent: * \nDisallow: /'
    return HttpResponse(output_string)