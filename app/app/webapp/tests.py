# -*- coding: utf-8 -*-
from __future__ import unicode_literals
from django.test import TestCase
from django.urls import reverse
from rest_framework.test import APIClient, APITestCase
from rest_framework import status
import logging
from models import *
from serializers import *


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

    def test_by_bank_state_return_None(self):
        bank = Bank(name="Test Bank", url_name="TB")
        bank.save()
        bank1 = Bank(name="Test Bank1", url_name="TB1")
        bank1.save()
        district = District(name="District Name", url_name="DN")
        state = State(name="Not a state", url_name="NAS")
        state.save()
        district.state = state
        district.bank.add(bank)
        district.save()
        district_list = district.by_bank_and_state(bank1, state)
        self.assertFalse(district_list)

    def test_by_bank_state_return_list(self):
        bank = Bank(name="Test Bank", url_name="TB")
        bank.save()
        district = District(name="District Name", url_name="DN")
        state = State(name="Not a state", url_name="NAS")
        state.save()
        district.state = state
        district.bank.add(bank)
        district.save()
        district_list = district.by_bank_and_state(bank, state)
        logging.info("District list size = %s", len(district_list))
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

    def test_by_bank_district_return_None(self):
        bank = Bank(name="Test Bank", url_name="TB")
        bank.save()
        bank1 = Bank(name="Test Bank1", url_name="TB1")
        bank1.save()
        district = District(name="District Name", url_name="DN")
        state = State(name="Not a state", url_name="NAS")
        state.save()
        district.state = state
        district.save()
        city = City(name="City Name", url_name="CN")
        city.district = district
        city.bank.add(bank)
        city.save()
        city_list = city.by_bank_and_district(bank1, district)
        self.assertFalse(city_list)

    def test_by_bank_district_return_list(self):
        bank = Bank(name="Test Bank", url_name="TB")
        bank.save()
        district = District(name="District Name", url_name="DN")
        state = State(name="Not a state", url_name="NAS")
        state.save()
        district.state = state
        district.save()
        city = City(name="City Name", url_name="CN")
        city.district = district
        city.bank.add(bank)
        city.save()
        city_list = city.by_bank_and_district(bank, district)
        self.assertTrue(len(city_list))


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


class ApiTesting(APITestCase):
    def setUp(self):
        url = reverse('api_bank_detail')
        data = [{
                "bank": "AXIS BANK",
                "ifsc": "UTIB0VNSBL1",
                "micr": "NA",
                "branch": "THE VEJALPUR NAGRIK SAHAKARI BANK",
                "address": "VEJALPUR BANK ROAD TALUKA KALOL 389340",
                "contact": "234544",
                "city": "VEJALPORE",
                "district": "PANCH MAHALS",
                "state": "GUJARAT"
            },
            {
                "bank": "AXIS BANK",
                "ifsc": "UTIB0SWUCB1",
                "micr": "NA",
                "branch": "WARANGAL URBAN COOP BANK LTD",
                "address": "11 15 107 O CITY ROAD KASHIBUGGA WARANGAL 506002",
                "contact": "2440143",
                "city": "WARANGAL",
                "district": "WARANGAL",
                "state": "TELANGANA"
            },
            {
                "bank": "AXIS BANK",
                "ifsc": "UTIB0SWJP01",
                "micr": "NA",
                "branch": "WARDHA ZILAPARISHAD EMPLOYEES URBAN",
                "address": "MAHADEOPURA MASJID CHOWK WARDHA 442101",
                "contact": "243469",
                "city": "WARDHA",
                "district": "WARDHA",
                "state": "MAHARASHTRA"
            },
            {
                "bank": "CITI BANK",
                "ifsc": "CITI0000047",
                "micr": "NA",
                "branch": "SERILINGAMPALLY",
                "address": "CITIBANK NA-UNIT NO.1, APURUPA SILPI, SURVEY NO. 124, GACHIBOWLI VILLAGE, SERILINGAMPALLY MANDAL, HYDERABAD, RANGA REDDY DISTRICT, ANDHRA PRADESH - 500032",
                "contact": "66017109",
                "city": "HYDERABAD",
                "district": "RANGA REDDY DISTRICT",
                "state": "ANDHRA PRADESH"
            },
            {
                "bank": "HSBC BANK",
                "ifsc": "HSBC0600002",
                "micr": "NA",
                "branch": "MAIN BRANCH  CHENNAI",
                "address": "96  DR RADHAKRISHNAN SALAI  MYLAPORE  CHENNAI 600 004",
                "contact": "0",
                "city": "CHENNAI",
                "district": "CHENNAI",
                "state": "TAMIL NADU"
            },
            {
                "bank": "HSBC BANK",
                "ifsc": "HSBC0400012",
                "micr": "NA",
                "branch": "POWAI  MUMBAI",
                "address": "PRUDENTIAL HOUSE  GROUND FLOOR  HIRANANDANI BUSINESS PARK  POWAI  MUMBAI 400 076",
                "contact": "0",
                "city": "MUMBAI",
                "district": "GREATER MUMBAI",
                "state": "MAHARASHTRA"
            },
            {
                "bank": "HSBC BANK",
                "ifsc": "HSBC0560003",
                "micr": "NA",
                "branch": "JAYANAGAR  BANGALORE",
                "address": "SURAJ GANGA ARCADE  332 BY 7  14TH CROSS  II BLOCK  JAYANAGAR  BANGALORE 560 011",
                "contact": "0",
                "city": "BANGALORE",
                "district": "BANGALORE URBAN",
                "state": "KARNATAKA"
            },
            {
                "bank": "HSBC BANK",
                "ifsc": "HSBC0500002",
                "micr": "NA",
                "branch": "HYDERABAD",
                "address": "UMA PLAZARAJAGOPAL TOWERS  ROAD NO 1  NAGARJUNA HILLS  HYDERABAD  500 082",
                "contact": "0",
                "city": "HYDERABAD",
                "district": "HYDERABAD URBAN",
                "state": "ANDHRA PRADESH"
            },
            {
                "bank": "HSBC BANK",
                "ifsc": "HSBC0400010",
                "micr": "NA",
                "branch": "LOKHANDWALA  MUMBAI",
                "address": "E 2 BY 3 BY 4  MANISH GARDEN  GROUND FLOOR  J P ROAD  ANDHERI  WMUMBAI 400 053",
                "contact": "0",
                "city": "MUMBAI",
                "district": "GREATER MUMBAI",
                "state": "MAHARASHTRA"
            },
            {
                "bank": "HSBC BANK",
                "ifsc": "HSBC0411002",
                "micr": "NA",
                "branch": "PUNE",
                "address": "AMAR AVINASH CORPORATE CITY  SECTOR NO  11  BUND GARDEN ROAD  PUNE 411 011",
                "contact": "0",
                "city": "PUNE",
                "district": "PUNE",
                "state": "MAHARASHTRA"
            },
            {
                "bank": "HSBC BANK",
                "ifsc": "HSBC0400008",
                "micr": "NA",
                "branch": "JUHU  MUMBAI",
                "address": "SAROJ  B7  KAPOLE SOCIETY  V L MEHTA ROAD  JUHU  MUMBAI 400 049",
                "contact": "0",
                "city": "MUMBAI",
                "district": "GREATER MUMBAI",
                "state": "MAHARASHTRA"
            },
            {
                "bank": "HSBC BANK",
                "ifsc": "HSBC0560002",
                "micr": "NA",
                "branch": "MAIN BRANCH  BANGALORE",
                "address": "NO  7  MAHATMA GANDHI ROAD  BANGALORE 560 001",
                "contact": "0",
                "city": "BANGALORE",
                "district": "BANGALORE URBAN",
                "state": "KARNATAKA"
            },
            {
                "bank": "FEDERAL BANK",
                "ifsc": "FDRL0WDCB01",
                "micr": "NA",
                "branch": "THE WAYANAD DIST CO-OP BANK LT",
                "address": "THE WAYANAD DISTRICT CO-OPERATIVE BANK LTD KALPETTANORTH SUB-MEMBER",
                "contact": "0",
                "city": "KALPETTA",
                "district": "WAYANAD",
                "state": "KERALA"
            },
            {
                "bank": "BANK OF INDIA",
                "ifsc": "BKID0BESCOM",
                "micr": "NA",
                "branch": "BESCOM CANTONMENT",
                "address": "JYOTHI MAHAL 49 ST MARKS ROAD CANTONMENT BANGALORE 560001",
                "contact": "22959473",
                "city": "BANGALORE URBAN",
                "district": "BANGALORE",
                "state": "KARNATAKA"
            },
            {
                "bank": "BANK OF INDIA",
                "ifsc": "BKID0JHARGB",
                "micr": "NA",
                "branch": "JHARKHAND GB-RANCHI BR",
                "address": "NEAR OVERBRIDGE, 5, MAIN ROAD, RANCHI- 834001",
                "contact": "9431996700",
                "city": "RANCHI",
                "district": "RANCHI",
                "state": "JHARKHAND"
            },
            {
                "bank": "BANK OF INDIA",
                "ifsc": "BKID0WAINGB",
                "micr": "NA",
                "branch": "VIDHARBHA KONKAN GRAMIN BANK HO NAGPUR",
                "address": "OLD HUDKESHWAR NAKA NAGPUR-440034",
                "contact": "2267447000",
                "city": "SOLAPUR",
                "district": "SOLAPUR",
                "state": "MAHARASHTRA"
            },
            {
                "bank": "ALLAHABAD BANK",
                "ifsc": "ALLA0AU1662",
                "micr": "NA",
                "branch": "ALLAHABAD UP GRAMIN BANK BHEDIARI",
                "address": "BHEDIARI,NEWASI,HUZURPUR,KAISERGANJ,271872",
                "contact": "8052302833",
                "city": "KAISARGANJ",
                "district": "BAHRAICH",
                "state": "UTTAR PRADESH"
            }
            ]
        res = self.client.post(url,data,format='json')
        logging.info("SET-UP DONE.")

    def test_api_bank(self):
        url = reverse('api_bank')
        res = self.client.get(url)
        self.assertEqual(res.status_code, status.HTTP_200_OK)

    def test_api_state(self):
        url = reverse('api_state')
        res = self.client.get(url, {'bank_id': 1})
        self.assertEqual(res.status_code, status.HTTP_200_OK)

    def test_api_district(self):
        url = reverse('api_district')
        res = self.client.get(url, {'bank_id': 1, 'state_id': 1})
        self.assertEqual(res.status_code, status.HTTP_200_OK)

    def test_api_city(self):
        url = reverse('api_city')
        res = self.client.get(url, {'bank_id': 1, 'district_id': 1})
        self.assertEqual(res.status_code, status.HTTP_200_OK)

    def test_api_branch(self):
        url = reverse('api_branch')
        res = self.client.get(url,{'bank_id': 1, 'city_id': 1})
        self.assertEqual(res.status_code, status.HTTP_200_OK)

class BankSerializserTestCase(TestCase):
    def test_bank_serializer(self):
        bank = Bank(name="Test Bank", url_name="TB")
        serializer = BankSerializer(bank)
        self.assertEqual("Test Bank",serializer.data['name'])

class StateSerializerTestCase(TestCase):
    def test_state_serializer(self):
        bank = Bank(name="Test Bank", url_name="TB")        
        bank.save()
        state = State(name="Test State", url_name="TS")
        state.bank.add(bank)
        state.save()
        serializer = StateSerializer(state)        
        self.assertEqual(serializer.data['bank'][0].items()[2][1],'Test Bank')


class SeoComponentTestCase(TestCase):
    def test_seo_component(self):
        seo_component = SeoComponent(name='seo_component',template_type="TEXT",heading="page heading",content="This is home page")
        serializer = SeoComponentSerializer(seo_component)
        self.assertEqual(serializer.data['name'],"seo_component")

class SeoComponentGroupTestCase(TestCase):
    def test_seo_component_group(self):
        seo_component = SeoComponent(name='seo_component',template_type="TEXT",heading="page heading",content="This is home page")
        seo_component.save()
        group = SeoComponentGroup(name="home_page_group")
        group.components.add(seo_component)        
        serializer = SeoComponentGroupSerializer(group)
        logging.info(serializer.data['components'])
        self.assertEqual(serializer.data['components'][0].items()[1][1],"seo_component")

class PageTestCase(TestCase):
    def test_page(self):
        seo_component = SeoComponent(name='seo_component',template_type="TEXT",heading="page heading",content="This is home page")
        seo_component.save()
        group = SeoComponentGroup(name="home_page_group")
        group.components.add(seo_component)
        group.save()
        page = Page(name="home_page",component_group=group)
        serializer = PageSerializer(page)        
        self.assertEqual(serializer.data['component_group']['name'],'home_page_group')
