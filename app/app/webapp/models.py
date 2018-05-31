# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models


class Bank(models.Model):
    name = models.CharField(max_length=200, default='')
    image_url = models.CharField(max_length=200, default='', blank=True)
    acronym = models.CharField(max_length=50, default='', blank=True)
    seo_content_top = models.TextField(max_length=1000, blank=True)
    seo_content_middle = models.TextField(max_length=100, blank=True)
    seo_content_bottom = models.TextField(max_length=100, blank=True)

    def __str__(self):
        return self.name
