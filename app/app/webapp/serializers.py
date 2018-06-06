from rest_framework import serializers
from models import Bank, BranchDetail


class ApiInfoSerializer(serializers.Serializer):
    info = serializers.CharField(max_length=160)


class BankSerializer(serializers.ModelSerializer):
    class Meta:
        model = Bank
        fields = '__all__'


class BranchDetailSerializer(serializers.ModelSerializer):
    class Meta:
        model = BranchDetail
        fields = '__all__'


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
