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
    return render(request, 'aboutus.html')


def disclaimer(request):
    return render(request, 'disclaimer.html')


def privacy(request):
    return render(request, 'privacy.html')


def contactus(request):
    return render(request, 'contactus.html')


def by_ifsc(request):
    return render(request, 'by_ifsc.html')
