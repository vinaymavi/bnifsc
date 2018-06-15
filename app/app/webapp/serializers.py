from rest_framework import serializers
from models import Bank, BranchDetail, State, District, City


class ApiInfoSerializer(serializers.Serializer):
    info = serializers.CharField(max_length=160)


class BankSerializer(serializers.ModelSerializer):
    _id = serializers.SerializerMethodField('get_custom_id')
    class Meta:
        model = Bank
        fields = '__all__'

    def get_custom_id(self, obj):
        return obj.state_id
        

class BranchDetailSerializer(serializers.ModelSerializer):
    _id = serializers.SerializerMethodField('get_custom_id')
    class Meta:
        model = BranchDetail
        fields = '__all__'    
    
    def get_custom_id(self, obj):
        return obj.branch_id

class StateSerializer(serializers.ModelSerializer):
    _id = serializers.SerializerMethodField('get_custom_id')

    class Meta:
        model = State
        fields = '__all__'

    def get_custom_id(self, obj):
        return obj.state_id


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
