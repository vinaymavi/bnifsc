# -*- coding: utf-8 -*-
from __future__ import unicode_literals
from django.test import TestCase
import logging
from models import Bank,State


class BankTestCase(TestCase):
    def test_save_bank(self):     
        bank = Bank(name="Test Bank",url_name="TB")
        bank.save()
        try:
            new_bank = Bank.objects.get(url_name="TB")
            self.assertEqual(new_bank.name,"Test Bank")
        except Bank.DoesNotExist: 
            self.assertEqual(new_bank.name,"Test Bank")

    def test_get_by_name(self):
        bank = Bank(name="Test Bank",url_name="TB")
        bank.save()
        new_bank = Bank().get_by_name('Test Bank')
        self.assertEqual(new_bank.url_name,"TB")    

class StateTestCase(TestCase):
    def test_get_state_by_bank_id_return_none(self):
        banks = State().by_bank_id(123444444)        
        self.assertFalse(banks)
    
    def test_get_state_by_bank_id_return_list(self):
        bank = Bank(name="Test Bank",url_name="TB")
        bank.save()        
        state = State(name="Test State",url_name="TS")
        state.bank.add(bank)
        state.save()
        state = State(name="Test State Two",url_name="TST")
        state.bank.add(bank)
        state.save()
        state_list = State().by_bank_id(bank.id)
        logging.info("State List size %s",len(state_list))
        self.assertTrue(state_list)
    
    def test_get_by_state_name_return_none(self):
        state = State().by_state_name("Test State")
        self.assertFalse(state)
    
    def test_get_by_state_name_return_list(self):
        bank = Bank(name="Test Bank",url_name="TB")
        bank.save()        
        state = State(name="Test State",url_name="TS")
        state.bank.add(bank)
        state.save()
        state = State(name="Test State Two",url_name="TST")
        state.bank.add(bank)
        state.save()
        state = State().by_state_name('Test State Two')
        self.assertTrue(state)

    def test_add_bank_to_state(self):
        bank = Bank(name="Test Bank",url_name="TB")
        bank.save()        
        state = State(name="Test State",url_name="TS")
        state.save()
        state = State.add_bank_to_state(state,bank)        
        self.assertTrue(len(list(state.bank.filter())))

    def test_get_by_bank_and_state_return_none(self):
        bank = Bank(name="Not a bank",url_name="NAB")
        state = State(name="Not a state", url_name="NAS")
        new_bank = state.by_bank_and_state(bank,state.name)
        self.assertFalse(new_bank)
    
    def test_get_by_bank_and_state_return_bank(self):
        bank = Bank(name="Not a bank",url_name="NAB")
        bank.save()
        state = State(name="Not a state", url_name="NAS")
        state.save()
        new_bank = state.by_bank_and_state(bank,state.name)
        self.assertTrue(new_bank)

