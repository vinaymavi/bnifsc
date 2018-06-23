# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models
from djangae import fields
from django.utils import timezone
import logging

SEO_TEMPLATE_TYPE = (('TEMPLATE', 'TEMPLATE'), ('TEXT', 'TEXT'))


# TODO Seo should be a Seprate model and have link with others.

class Bank(models.Model):
    name = fields.CharField(max_length=200)
    url_name = fields.CharField(max_length=200)
    bank_id = models.PositiveIntegerField(default=0)
    image_url = fields.CharField(max_length=200, default='', blank=True)
    acronym = fields.CharField(max_length=50, default='', blank=True)
    help_line_number = fields.CharField(max_length=20, blank=True)
    support_email = fields.CharField(max_length=100, blank=True)
    is_top_list = models.BooleanField(default=False)
    add_date = models.DateTimeField(default=timezone.now())
    update_date = models.DateTimeField(default=timezone.now())

    def get_by_name(self, name):
        try:
            return Bank.objects.get(name=name)
        except Bank.DoesNotExist:
            return None

    def get_by_bank_id(self, bank_id):
        try:
            return Bank.objects.get(bank_id=bank_id)
        except Bank.DoesNotExist:
            return None

    def list_all(self):
        return Bank.objects.all().order_by('-is_top_list', 'name')

    def __str__(self):
        return self.name


class State(models.Model):
    name = fields.CharField(max_length=200, unique=True)
    bank = fields.RelatedSetField(Bank)
    url_name = fields.CharField(max_length=50)
    state_id = models.PositiveIntegerField(default=0)
    add_date = models.DateTimeField(default=timezone.now())
    update_date = models.DateTimeField(default=timezone.now())

    def __str__(self):
        return self.name

    def by_bank_and_state(self, bank, state_name):
        """
        return state with bank and state_name 
        if bank name is not added it will add it to state and return.
        """
        try:
            return State.objects.get(bank__overlap=[bank.id], name=state_name)
        except State.DoesNotExist:
            state = self.by_state_name(state_name)
            if state is None:
                return None
            else:
                return State.add_bank_to_state(state, bank)

    @staticmethod
    def add_bank_to_state(state, bank):
        """
        Add provided bank to state and return state.
        """
        state.bank.add(bank)
        state.save()
        return state

    def by_bank_id(self, bank_id):
        """
        This function accepts a bank_id an return list of State or None if not found.
        """
        query_set = State.objects.filter(bank__overlap=[bank_id])
        if query_set.count() > 0:
            return list(query_set)
        else:
            logging.warn('state does not exist for bank id=%s', bank_id)
            return None

    def by_state_name(self, state_name):
        """
        This function accept a state_name and return list of object of state or None if not found.
        """
        try:
            return State.objects.get(name=state_name)
        except State.DoesNotExist:
            logging.warn('state name = %s does not exist', state_name)
            return None

    def by_state_id(self, state_id):
        """
        This function return State by state id or None.
        """
        try:
            return State.objects.get(state_id=state_id)
        except State.DoesNotExist as err:
            logging.warn("State doest not exist for state_id=%s", state_id)
            return None


class District(models.Model):
    name = fields.CharField(max_length=200)
    url_name = fields.CharField(max_length=50)
    district_id = models.PositiveIntegerField(default=0)
    state = models.ForeignKey(State)
    add_date = models.DateTimeField(default=timezone.now())
    update_date = models.DateTimeField(default=timezone.now())
    bank = fields.RelatedSetField(Bank)

    def __str__(self):
        return self.name

    def by_state_and_district(self, state, district_name):
        """
        Return district by state and district name if state is not linked it will link or 
        None if district does not exist.
        """
        try:
            return District.objects.get(state=state, name=district_name)
        except District.DoesNotExist:
            logging.warn(
                "State name='%s' with district = '%s' does not exist.", state.name, district_name)
            return None

    def by_name(self, district_name):
        """
        Return district by name or None if district not found
        """
        try:
            return District.objects.get(name=district_name)
        except District.DoesNotExist:
            logging.warn("District name ='%s' does not exist.", district_name)
            return None

    def by_bank_and_state(self, bank, state):
        """
        Return district list by state or None if district not found.
        """
        query_set = District.objects.filter(
            bank__overlap=[bank.pk], state=state)
        if query_set.count() > 0:
            return query_set
        else:
            logging.warn('district does not exist for state =%s', state.name)
            return None

    def by_district_id(self, district_id):
        try:
            return District.objects.get(district_id=district_id)
        except District.DoesNotExist:
            logging.warn("District does not exist district_id=%s", district_id)


class City(models.Model):
    name = fields.CharField(max_length=200)
    url_name = fields.CharField(max_length=50)
    city_id = models.PositiveIntegerField(default=0)
    district = models.ForeignKey(District)
    add_date = models.DateTimeField(default=timezone.now())
    update_date = models.DateTimeField(default=timezone.now())
    bank = fields.RelatedSetField(Bank)

    def __str__(self):
        return self.name

    def by_district_and_city(self, district, city_name):
        """
        Return city by district and city name or None if does not exist.
        """
        try:
            return City.objects.get(district=district, name=city_name)
        except City.DoesNotExist:
            logging.warn(
                "City with district='%s' with city='%s' does not exist", district.name, city_name)
            return None

    def by_bank_and_district(self, bank, district):
        """
        Return list of city or none.
        """
        query_set = City.objects.filter(
            bank__overlap=[bank.pk], district=district)
        if query_set.count() > 0:
            return query_set
        else:
            logging.warn('city does not exist for district =%s', district.name)
            return None

    def by_city_id(self, city_id):
        try:
            return City.objects.get(city_id=city_id)
        except City.DoesNotExist:
            logging.info("City does not exist, city_id=%s", city_id)


class BranchDetail(models.Model):
    bank = models.ForeignKey(Bank)
    name = fields.CharField(max_length=100)
    branch_id = models.PositiveIntegerField(default=0)
    branch_url_name = fields.CharField(max_length=100)
    ifsc_code = fields.CharField(max_length=20)
    micr = fields.CharField(max_length=10, blank=True)
    swift = fields.CharField(max_length=10, blank=True)
    state = models.ForeignKey(State)
    district = models.ForeignKey(District)
    city = models.ForeignKey(City)
    address = models.TextField(max_length=500)
    email = fields.CharField(max_length=500, blank=True)
    mobile = fields.CharField(max_length=500, blank=True)
    land_line = fields.CharField(max_length=500, blank=True)
    pin = fields.CharField(max_length=500, blank=True)
    is_verified = models.BooleanField(default=False)
    add_date = models.DateTimeField(default=timezone.now())
    update_date = models.DateTimeField(default=timezone.now())

    def __str__(self):
        return self.name

    def by_city(self, city):

        query_set = BranchDetail.objects.filter(city=city)
        if query_set.count() > 0:
            return query_set
        else:
            logging.warn('Bank does not exist for city =%s', city.name)
            return None

    def by_ifsc(self, ifsc_code):
        try:
            return BranchDetail.objects.get(ifsc_code=ifsc_code)
        except BranchDetail.DoesNotExist as er:
            logging.warn(
                "Branch with ifsc code = '%s' does not exist.", ifsc_code)
            return None

    def by_bank_and_city(self, bank, city):
        """
        Return list of city or none.
        """
        query_set = BranchDetail.objects.filter(bank=bank, city=city)
        if query_set.count() > 0:
            return query_set
        else:
            logging.warn('city does not exist for city =%s', city.name)
            return None

    def by_branch_id(self, branch_id):
        """
        Return Branch or None
        """
        try:
            return BranchDetail.objects.get(branch_id=branch_id)
        except BranchDetail.DoesNotExist:
            logging.warn("Branch does not exist with branch_id=%s", branch_id)
            return None


class SeoComponent(models.Model):
    name = fields.CharField(max_length=100, unique=True)
    template_type = fields.CharField(max_length=20, choices=SEO_TEMPLATE_TYPE)
    heading = fields.CharField(max_length=200)
    content = models.TextField(max_length=1000)
    add_date = models.DateTimeField(default=timezone.now())
    update_date = models.DateTimeField(default=timezone.now())

    def __str__(self):
        return self.name


class SeoComponentGroup(models.Model):
    name = fields.CharField(max_length=100, unique=True)
    components = fields.RelatedSetField(SeoComponent)
    add_date = models.DateTimeField(default=timezone.now())
    update_date = models.DateTimeField(default=timezone.now())

    def __str__(self):
        return self.name


class AppInfo(models.Model):
    info = models.CharField(max_length=160)


class Counter(models.Model):
    bank = models.PositiveIntegerField(default=0)
    state = models.PositiveIntegerField(default=0)
    district = models.PositiveIntegerField(default=0)
    city = models.PositiveIntegerField(default=0)
    branch = models.PositiveIntegerField(default=0)

    @staticmethod
    def next_bank_id(counter):
        counter.bank += 1
        counter.save()
        return counter.bank

    @staticmethod
    def next_state_id(counter):
        counter.state += 1
        counter.save()
        return counter.state

    @staticmethod
    def next_district_id(counter):
        counter.district += 1
        counter.save()
        return counter.district

    @staticmethod
    def next_city_id(counter):
        counter.city += 1
        counter.save()
        return counter.city

    @staticmethod
    def next_branch_id(counter):
        counter.branch += 1
        counter.save()
        return counter.branch

    @staticmethod
    def get_counter():
        query_set = Counter.objects.all()
        if query_set.count() > 0:
            return query_set.first()
        else:
            counter = Counter()
            counter.save()
            return Counter.get_counter()


class PageUrlTemplate(models.Model):
    name = fields.CharField(max_length=100, unique=True)
    template = models.TextField(max_length=250)
    add_date = models.DateTimeField(default=timezone.now())
    update_date = models.DateTimeField(default=timezone.now())

    def __str__(self):
        return self.name


class PageHeadingTemplate(models.Model):
    name = fields.CharField(max_length=100, unique=True)
    h1_template = models.TextField(max_length=250)
    h2_template = models.TextField(max_length=250)
    add_date = models.DateTimeField(default=timezone.now())
    update_date = models.DateTimeField(default=timezone.now())

    def __str__(self):
        return self.name


class HeaderSeoComponent(models.Model):
    name = fields.CharField(max_length=60)
    title = fields.CharField(max_length=60)
    meta_keywords = fields.CharField(max_length=260)
    meta_description = fields.CharField(max_length=260)
    canonical_url = fields.CharField(max_length=260,blank=True)
    add_date = models.DateTimeField(default=timezone.now())
    update_date = models.DateTimeField(default=timezone.now())

    def __str__(self):
        return self.name


class Page(models.Model):
    name = fields.CharField(max_length=100, unique=True)
    component_group = models.ForeignKey(SeoComponentGroup)
    header_seo_component = models.ForeignKey(HeaderSeoComponent)
    url_template = models.ForeignKey(PageUrlTemplate, blank=True, null=True)
    heading_template = models.ForeignKey(PageHeadingTemplate)
    add_date = models.DateTimeField(default=timezone.now())
    update_date = models.DateTimeField(default=timezone.now())

    def __str__(self):
        return self.name

    @staticmethod
    def by_page_name(page_name):
        try:
            return Page.objects.get(name=page_name)
        except Page.DoesNotExist as err:
            logging.warning("Page name=%s does not exist ", page_name)
            return None
