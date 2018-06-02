from rest_framework import serializers


class InfoSerializer(serializers.Serializer):
    info = serializers.CharField(max_length=160)
