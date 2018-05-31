# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models
from djangae import fields


class Bank(models.Model):
    name = fields.CharField(max_length=200, default='')
    image_url = fields.CharField(max_length=200, default='', blank=True)
    acronym = fields.CharField(max_length=50, default='', blank=True)
    seo_content_top = models.TextField(max_length=1000, blank=True)
    seo_content_middle = models.TextField(max_length=100, blank=True)
    seo_content_bottom = models.TextField(max_length=100, blank=True)
    help_line_number = fields.CharField(max_length=20, blank=True)
    support_email = fields.CharField(max_length=100, blank=True)
    is_top_list = models.BooleanField(default=False)

    def __str__(self):
        return self.name


class BankDetail(models.Model):
    bank = models.ForeignKey(Bank)
    branch_name = fields.CharField(max_length=100)
    ifsc_code = fields.CharField(max_length=20)
    micr = fields.CharField(max_length=10, blank=True)
    swift = fields.CharField(max_length=10, blank=True)
    state = fields.CharField(max_length=200)
    district = fields.CharField(max_length=200)
    city = fields.CharField(max_length=200)
    address = models.TextField(max_length=500)
    email = fields.CharField(max_length=500, blank=True)
    mobile = fields.CharField(max_length=500, blank=True)
    land_line = fields.CharField(max_length=500, blank=True)
    pin = fields.CharField(max_length=500, blank=True)
    is_verified = models.BooleanField(default=False)
    def __str__(self):
        return self.branch_name
