from rest_framework import serializers
from models import Bank


class ApiInfoSerializer(serializers.Serializer):
    info = serializers.CharField(max_length=160)


class BankSerializer(serializers.ModelSerializer):
    class Meta:
        model = Bank
        fields = '__all__'
