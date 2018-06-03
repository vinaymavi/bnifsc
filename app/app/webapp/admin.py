# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.contrib import admin
from models import Bank, BranchDetail, Seo, State, District, City
from django.conf import settings

# Register your models here.
admin.AdminSite.site_header = settings.SITE_ADMIN_HEADER
admin.AdminSite.site_title = settings.SITE_ADMIN_TITLE

admin.site.register(Bank)
admin.site.register(BranchDetail)
admin.site.register(Seo)
admin.site.register(State)
admin.site.register(District)
admin.site.register(City)
