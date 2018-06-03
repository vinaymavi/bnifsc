# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.contrib import admin
from models import Bank, BranchDetail, Seo, State, District, City

# Register your models here.

admin.site.register(Bank)
admin.site.register(BranchDetail)
admin.site.register(Seo)
admin.site.register(State)
admin.site.register(District)
admin.site.register(City)

