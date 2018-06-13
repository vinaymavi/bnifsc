# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.shortcuts import render

from models import Bank
import logging


def index(request):
    banks = Bank().list_all()
    context = {"banks": banks}
    logging.info("Banks count = %s" % (len(context["banks"])))
    return render(request, 'base.html', context=context)
