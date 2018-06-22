from rest_framework import serializers
from models import *
from django.conf import settings
from django.template import Template, Context
from django.urls import reverse


class ApiInfoSerializer(serializers.Serializer):
    info = serializers.CharField(max_length=160)


class BankSerializer(serializers.ModelSerializer):
    _id = serializers.SerializerMethodField('get_custom_id')
    url = serializers.SerializerMethodField()

    class Meta:
        model = Bank
        fields = '__all__'

    def get_custom_id(self, obj):
        return obj.bank_id

    def get_url(self, obj):
        """
        Generate URL for bank.
        """
        page = Page.by_page_name(settings.URL_PAGE_MAPPING['BANK_PAGE'])
        serializer = PageSerializer(page)
        logging.info(serializer.data['url_template']['template'])
        template = Template(serializer.data['url_template']['template'])
        context = Context({'url_name': "-".join(obj.url_name.split(' '))})
        url_str = template.render(context)
        logging.info('url string = {}'.format(url_str))
        return reverse('bank_page', args=[url_str, obj.bank_id])


class BranchDetailSerializer(serializers.ModelSerializer):
    _id = serializers.SerializerMethodField('get_custom_id')

    class Meta:
        model = BranchDetail
        fields = '__all__'

    def get_custom_id(self, obj):
        return obj.branch_id


class StateSerializer(serializers.ModelSerializer):
    _id = serializers.SerializerMethodField('get_custom_id')
    bank = BankSerializer(many=True, read_only=True)
    url = serializers.SerializerMethodField()

    class Meta:
        model = State
        fields = '__all__'

    def get_custom_id(self, obj):
        return obj.state_id

    def get_url(self, obj):
        """
        Generate URL for state.
        """
        bank_id = self.context.get('bank_id')
        bank_name = self.context.get('bank_name')
        page = Page.by_page_name(settings.URL_PAGE_MAPPING['STATE_PAGE'])
        serializer = PageSerializer(page)
        logging.info(serializer.data['url_template']['template'])
        template = Template(serializer.data['url_template']['template'])
        context = Context({'state_name': "-".join(obj.url_name.split(' ')),
                           'bank_name': "-".join(bank_name.split(' '))})
        url_str = template.render(context)
        logging.info('url string = {}'.format(url_str))
        return reverse('state_page', args=[url_str, bank_id, obj.state_id])


class DistrictSerializer(serializers.ModelSerializer):
    _id = serializers.SerializerMethodField('get_custom_id')

    class Meta:
        model = District
        fields = '__all__'

    def get_custom_id(self, obj):
        return obj.district_id


class CitySerializer(serializers.ModelSerializer):
    _id = serializers.SerializerMethodField('get_custom_id')

    class Meta:
        model = City
        fields = '__all__'

    def get_custom_id(self, obj):
        return obj.city_id


class BankDetailSerializer(serializers.Serializer):
    bank = serializers.CharField(max_length=200)
    ifsc = serializers.CharField(max_length=200)
    micr = serializers.CharField(max_length=200)
    branch = serializers.CharField(max_length=200)
    address = serializers.CharField(max_length=1000)
    contact = serializers.CharField(max_length=1000)
    city = serializers.CharField(max_length=1000)
    district = serializers.CharField(max_length=1000)
    state = serializers.CharField(max_length=1000)


class SeoComponentSerializer(serializers.ModelSerializer):
    class Meta:
        model = SeoComponent
        fields = '__all__'


class SeoComponentGroupSerializer(serializers.ModelSerializer):
    components = SeoComponentSerializer(many=True, read_only=True)

    class Meta:
        model = SeoComponentGroup
        fields = '__all__'


class PageUrlTemplateSerializer(serializers.ModelSerializer):
    class Meta:
        model = PageUrlTemplate
        fields = '__all__'


class PageHeadingTemplateSerializer(serializers.ModelSerializer):
    class Meta:
        model = PageHeadingTemplate
        fields = '__all__'


class PageSerializer(serializers.ModelSerializer):
    component_group = SeoComponentGroupSerializer(read_only=True)
    url_template = PageUrlTemplateSerializer(read_only=True)
    heading_template = PageHeadingTemplateSerializer(read_only=True)

    class Meta:
        model = Page
        fields = '__all__'
