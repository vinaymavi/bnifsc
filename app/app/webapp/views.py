# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render

from models import Bank, Page
import logging
from django.conf import settings
from serializers import PageSerializer


def index(request):
    banks = Bank().list_all()
    page = Page.by_page_name(settings.URL_PAGE_MAPPING['HOME_PAGE'])
    serializer = PageSerializer(page)
    context = {"banks": banks, 'seo_data': serializer.data}
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
