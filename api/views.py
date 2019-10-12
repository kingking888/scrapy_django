from django.shortcuts import get_object_or_404


from rest_framework.views import APIView
from rest_framework.decorators import permission_classes
from rest_framework import permissions
from rest_framework.response import Response

import datetime
import random

from all_news.models import News as AllNews
from all_news.models import Category as AllNewsCategory

from .serializers import AllNewsSerializer
from .serializers import AllNewsCategorySerializer

hours = 24
latest_hours = 1


@permission_classes((permissions.IsAuthenticated,))
class RecentApiView(APIView):
    def get(self, request):

        now = datetime.datetime.now()
        earlier = now - datetime.timedelta(hours=latest_hours)

        num_entities = AllNews.objects.filter(date__range=(earlier, now)).count()
        first_value = AllNews.objects.filter(date__range=(earlier, now))[:1].get().pk

        rand_entities = random.sample(range(num_entities), 20)
        rand_entities = [x + first_value-1 for x in rand_entities]

        news = AllNews.objects.filter(date__range=(earlier, now), pk__in=rand_entities)
        data = AllNewsSerializer(news, many=True).data
        return Response(data)


@permission_classes((permissions.IsAuthenticated,))
class NewsApiView(APIView):
    def get(self, request):

        now = datetime.datetime.now()
        earlier = now - datetime.timedelta(hours=hours)

        newses = AllNews.objects.filter(date__range=(earlier, now))
        data = AllNewsSerializer(newses, many=True).data
        return Response(data)


@permission_classes((permissions.IsAuthenticated,))
class NewsCategoryApiView(APIView):
    def get(self, request, name):

        now = datetime.datetime.now()
        earlier = now - datetime.timedelta(hours=hours)

        newses = AllNews.objects.filter(category__name=name, date__range=(earlier, now))
        data = AllNewsSerializer(newses, many=True).data
        return Response(data)


@permission_classes((permissions.IsAuthenticated,))
class DetailNewsApiView(APIView):
    def get(self, request, pk):

        now = datetime.datetime.now()
        earlier = now - datetime.timedelta(hours=hours)

        news = get_object_or_404(AllNews, pk=pk, date__range=(earlier, now))
        data = AllNewsSerializer(news).data
        return Response(data)


@permission_classes((permissions.IsAuthenticated,))
class AllCategoryApiView(APIView):
    def get(self, request):
        categories = AllNewsCategory.objects.all()
        data = AllNewsCategorySerializer(categories, many=True).data
        return Response(data)


