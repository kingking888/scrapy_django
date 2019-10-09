from rest_framework import serializers

from all_news.models import News as AllNews
from all_news.models import Category as AllNewsCategory


class AllNewsSerializer(serializers.ModelSerializer):

    class Meta:
        model = AllNews
        fields = ('__all__')


class AllNewsCategorySerializer(serializers.ModelSerializer):

    class Meta:
        model = AllNewsCategory
        fields = ('__all__')
