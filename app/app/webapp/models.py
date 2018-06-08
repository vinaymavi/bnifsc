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
    url_name = fields.CharField(max_length=200, unique=True)
    bank_id = fields.ComputedIntegerField(
        lambda self: self.bank_id if self.bank_id else len(Bank.objects.all()) + 1)
    image_url = fields.CharField(max_length=200, default='', blank=True)
    acronym = fields.CharField(max_length=50, default='', blank=True)
    seo_content_one = models.TextField(max_length=1000, blank=True)
    seo_content_two = models.TextField(max_length=100, blank=True)
    seo_content_three = models.TextField(max_length=100, blank=True)
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

    def __str__(self):
        return self.name


class State(models.Model):
    name = fields.CharField(max_length=200, unique=True)
    bank = fields.RelatedSetField(Bank)
    url_name = fields.CharField(max_length=50)
    state_id = fields.ComputedIntegerField(
        lambda self: self.state_id if self.state_id else len(State.objects.all()) + 1)
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
                return State.add_bank_to_state(state,bank)
                
    
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
        This function accepts a bank_id an return objects of State or None if not found.
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


class District(models.Model):
    name = fields.CharField(max_length=200)
    url_name = fields.CharField(max_length=50)
    district_id = fields.ComputedIntegerField(
        lambda self: self.district_id if self.district_id else len(District.objects.all()) + 1)
    state = models.ForeignKey(State)
    add_date = models.DateTimeField(default=timezone.now())
    update_date = models.DateTimeField(default=timezone.now())

    def __str__(self):
        return self.name

    def by_state_and_district(self, state, district):
        try:
            District.objects.get(state__overlap=[state.id], name=district)
        except District.DoesNotExist:
            return None


class City(models.Model):
    name = fields.CharField(max_length=200)
    url_name = fields.CharField(max_length=50)
    city_id = fields.ComputedIntegerField(
        lambda self: self.city_id if self.city_id else len(City.objects.all()) + 1)
    district = models.ForeignKey(District)
    add_date = models.DateTimeField(default=timezone.now())
    update_date = models.DateTimeField(default=timezone.now())

    def __str__(self):
        return self.name


class BranchDetail(models.Model):
    bank = models.ForeignKey(Bank)
    branch_name = fields.CharField(max_length=100)
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
        return self.branch_name


class Seo(models.Model):
    rule_name = fields.CharField(max_length=100, unique=True)
    rule_type = fields.CharField(max_length=20, choices=SEO_TEMPLATE_TYPE)
    rule_content = models.TextField(max_length=1000)
    add_date = models.DateTimeField(default=timezone.now())
    update_date = models.DateTimeField(default=timezone.now())

    def __str__(self):
        return self.rule_name


class AppInfo(models.Model):
    info = models.CharField(max_length=160)
