from rest_framework import serializers

from all_news.models import News as AllNews
from all_news.models import Category as AllNewsCategory


class AllNewsSerializerListView(serializers.ModelSerializer):
    # category = serializers.StringRelatedField()
    short_description = serializers.CharField()

    # def get_short_description(self, obj):
    #     return AllNews.description[:200]

    class Meta:
        model = AllNews
        fields = ['id', 'title', 'short_description', 'image', 'date']





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

