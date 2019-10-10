from rest_framework import serializers

from all_news.models import News as AllNews
from all_news.models import Category as AllNewsCategory


class AllNewsSerializer(serializers.ModelSerializer):
    category = serializers.StringRelatedField()

    class Meta:
        model = AllNews
        fields = ('__all__')


class AllNewsCategorySerializer(serializers.ModelSerializer):
    newses = AllNewsSerializer(many=True, read_only=True)

    class Meta:
        model = AllNewsCategory
        fields = ('name', 'newses')

