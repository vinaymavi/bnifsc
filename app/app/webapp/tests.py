# -*- coding: utf-8 -*-
from __future__ import unicode_literals
from django.test import TestCase
import logging
from models import Bank, State, District, City, BranchDetail


class BankTestCase(TestCase):
    def test_save_bank(self):
        bank = Bank(name="Test Bank", url_name="TB")
        bank.save()
        try:
            new_bank = Bank.objects.get(url_name="TB")
            self.assertEqual(new_bank.name, "Test Bank")
        except Bank.DoesNotExist:
            self.assertEqual(new_bank.name, "Test Bank")

    def test_get_by_name(self):
        bank = Bank(name="Test Bank", url_name="TB")
        bank.save()
        new_bank = Bank().get_by_name('Test Bank')
        self.assertEqual(new_bank.url_name, "TB")

    def test_get_by_id(self):
        bank = Bank(name="Test Bank", url_name="TB")
        bank.save()
        new_bank = Bank().get_by_bank_id(1)
        self.assertTrue(new_bank)


class StateTestCase(TestCase):
    def test_get_state_by_bank_id_return_none(self):
        banks = State().by_bank_id(123444444)
        self.assertFalse(banks)

    def test_get_state_by_bank_id_return_list(self):
        bank = Bank(name="Test Bank", url_name="TB")
        bank.save()
        state = State(name="Test State", url_name="TS")
        state.bank.add(bank)
        state.save()
        state = State(name="Test State Two", url_name="TST")
        state.bank.add(bank)
        state.save()
        state_list = State().by_bank_id(bank.id)
        logging.info("State List size %s", len(state_list))
        self.assertTrue(state_list)

    def test_get_by_state_name_return_none(self):
        state = State().by_state_name("Test State")
        self.assertFalse(state)

    def test_get_by_state_name_return_list(self):
        bank = Bank(name="Test Bank", url_name="TB")
        bank.save()
        state = State(name="Test State", url_name="TS")
        state.bank.add(bank)
        state.save()
        state = State(name="Test State Two", url_name="TST")
        state.bank.add(bank)
        state.save()
        state = State().by_state_name('Test State Two')
        self.assertTrue(state)

    def test_add_bank_to_state(self):
        bank = Bank(name="Test Bank", url_name="TB")
        bank.save()
        state = State(name="Test State", url_name="TS")
        state.save()
        state = State.add_bank_to_state(state, bank)
        self.assertTrue(len(list(state.bank.filter())))

    def test_get_by_bank_and_state_return_none(self):
        bank = Bank(name="Not a bank", url_name="NAB")
        state = State(name="Not a state", url_name="NAS")
        new_bank = state.by_bank_and_state(bank, state.name)
        self.assertFalse(new_bank)

    def test_get_by_bank_and_state_return_bank(self):
        bank = Bank(name="Not a bank", url_name="NAB")
        bank.save()
        state = State(name="Not a state", url_name="NAS")
        state.save()
        new_bank = state.by_bank_and_state(bank, state.name)
        self.assertTrue(new_bank)


class DistrictTestCase(TestCase):
    def test_by_name_return_none(self):
        district = District(name="District Name", url_name="DN")
        new_district = district.by_name("District Name")
        self.assertFalse(new_district)

    def test_by_name_return_district(self):
        district = District(name="District Name", url_name="DN")
        state = State(name="Not a state", url_name="NAS")
        state.save()
        district.state = state
        district.save()
        new_district = district.by_name("District Name")
        self.assertTrue(new_district)

    def test_by_state_and_district_return_None(self):
        district = District(name="District Name", url_name="DN")
        state = State(name="Not a state", url_name="NAS")
        new_district = district.by_state_and_district(state, "District Name")
        self.assertFalse(new_district)

    def test_by_state_and_district_return_district(self):
        district = District(name="District Name", url_name="DN")
        state = State(name="Not a state", url_name="NAS")
        state.save()
        district.state = state
        district.save()
        new_district = district.by_state_and_district(state, "District Name")
        self.assertTrue(new_district)

    def test_by_state_id(self):
        district = District(name="District Name", url_name="DN")
        state = State(name="Not a state", url_name="NAS")
        state.save()
        district.state = state
        district.save()
        district_list = district.by_sate(state)
        logging.info("District list size = %s",len(district_list))
        self.assertTrue(len(district_list))

class CityTestCase(TestCase):
    def test_by_district_and_city_return_none(self):
        district = District(name="District Name", url_name="DN")
        city = City().by_district_and_city(district, "No Name")
        self.assertFalse(city)

    def test_by_district_and_city_return_city(self):
        district = District(name="District Name", url_name="DN")
        state = State(name="Not a state", url_name="NAS")
        state.save()
        district.state = state
        district.save()
        city = City(name="City Name", url_name="CN")
        city.district = district
        city.save()
        new_city = City().by_district_and_city(district, "No Name")

        self.assertTrue(city)


class BranchDetailTestCase(TestCase):
    def test_by_city_return_none(self):
        city = City(name="City Name", url_name="CN")
        branch = BranchDetail().by_city(city)
        self.assertFalse(branch)

    def test_by_city_return_branch(self):
        bank = Bank(name="Test Bank", url_name="TB")
        bank.save()
        state = State(name="Not a state", url_name="NAS")
        state.save()
        district = District(name="District Name", url_name="DN")
        district.state = state
        district.save()
        city = City(name="City Name", url_name="CN")
        city.district = district
        city.save()
        branch = BranchDetail(name="Branch Name",
                              branch_url_name="BN", ifsc_code="1234567")
        branch.bank = bank
        branch.state = state
        branch.district = district
        branch.city = city
        branch.save()
        new_branch = BranchDetail().by_city(city)
        self.assertTrue(new_branch)
    
    def test_by_ifsc_return_branch(self):
        bank = Bank(name="Test Bank", url_name="TB")
        bank.save()
        state = State(name="Not a state", url_name="NAS")
        state.save()
        district = District(name="District Name", url_name="DN")
        district.state = state
        district.save()
        city = City(name="City Name", url_name="CN")
        city.district = district
        city.save()
        branch = BranchDetail(name="Branch Name",
                              branch_url_name="BN", ifsc_code="1234567")
        branch.bank = bank
        branch.state = state
        branch.district = district
        branch.city = city
        branch.save()
        new_branch = BranchDetail().by_ifsc("1234567")
        self.assertTrue(new_branch)
