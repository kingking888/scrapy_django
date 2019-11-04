from rest_framework import serializers

from all_news.models import News as AllNews
from all_news.models import Category as AllNewsCategory


class AllNewsSerializerListView(serializers.ModelSerializer):
    short_description = serializers.CharField()

    class Meta:
        model = AllNews
        fields = ['id', 'title', 'short_description', 'image', 'source', 'date']


class AllNewsSerializerRecentView(serializers.ModelSerializer):

    class Meta:
        model = AllNews
        fields = ['id', 'title', 'image', 'source', 'date']


class AllNewsSerializer(serializers.ModelSerializer):
    category = serializers.StringRelatedField()

    class Meta:
        model = AllNews
        fields = ('__all__')


class DetailNewsSerializer(serializers.ModelSerializer):

    class Meta:
        model = AllNews
        fields = ('description', 'url')


class AllNewsCategorySerializer(serializers.ModelSerializer):
    newses = AllNewsSerializer(many=True, read_only=True)

    class Meta:
        model = AllNewsCategory
        fields = ('name', 'newses')

