from django.shortcuts import get_object_or_404

from rest_framework import generics

from rest_framework.views import APIView
from rest_framework.decorators import api_view, permission_classes
from rest_framework import permissions
from rest_framework.response import Response

import datetime
import random

from all_news.models import News as AllNews
from all_news.models import Category as AllNewsCategory

from .serializers import AllNewsSerializer
from .serializers import AllNewsCategorySerializer

hours = 24


###########################Generic###########################################################
class AllNewsAPIView(generics.ListAPIView):
    now = datetime.datetime.now()
    earlier = now - datetime.timedelta(hours=hours)

    queryset = AllNews.objects.filter(date__range=(earlier, now))
    serializer_class = AllNewsSerializer


class HomePageApiView(generics.ListAPIView):
    now = datetime.datetime.now()
    earlier = now - datetime.timedelta(hours=hours)

    num_entities = AllNews.objects.filter(date__range=(earlier, now)).count()
    rand_entities = random.sample(range(num_entities), 20)

    queryset = AllNews.objects.filter(date__range=(earlier, now), pk__in=rand_entities)
    serializer_class = AllNewsSerializer


class AllNewsCategoryAPIView(generics.ListAPIView):
    queryset = AllNewsCategory.objects.all()
    serializer_class = AllNewsCategorySerializer



######################################### without generic ##############################################
@permission_classes((permissions.IsAdminUser,))
class HomePageApiViewRealTime(APIView):
    def get(self, request):

        now = datetime.datetime.now()
        earlier = now - datetime.timedelta(hours=hours)

        num_entities = AllNews.objects.filter(date__range=(earlier, now)).count()
        rand_entities = random.sample(range(num_entities), 5)

        news = AllNews.objects.filter(date__range=(earlier, now), pk__in=rand_entities)
        data = AllNewsSerializer(news, many=True).data
        return Response(data)


@permission_classes((permissions.IsAdminUser,))
class AllNewsApiViewRealtime(APIView):
    def get(self, request):

        now = datetime.datetime.now()
        earlier = now - datetime.timedelta(hours=hours)

        newses = AllNews.objects.filter(date__range=(earlier, now))
        data = AllNewsSerializer(newses, many=True).data
        return Response(data)


@permission_classes((permissions.IsAdminUser,))
class DetailNewsApiViewRealtime(APIView):
    def get(self, request, pk):

        now = datetime.datetime.now()
        earlier = now - datetime.timedelta(hours=hours)

        news = get_object_or_404(AllNews, pk=pk, date__range=(earlier, now))
        data = AllNewsSerializer(news).data
        return Response(data)
