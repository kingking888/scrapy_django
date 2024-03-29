from django.shortcuts import get_object_or_404


from rest_framework.views import APIView
from rest_framework import permissions, generics
from rest_framework.response import Response
from rest_framework.pagination import PageNumberPagination
from rest_framework import filters

from django.utils.decorators import method_decorator
from django.views.decorators.cache import cache_page

import datetime
import random

from all_news.models import News as AllNews
from all_news.models import Category as AllNewsCategory


from .serializers import AllNewsSerializerListView, AllNewsSerializerRecentView, AllNewsCategorySerializer, AllNewsSerializer, DetailNewsSerializer

hours = 24
latest_hours = 2


# ===================== Latest News V2 =======================

class RecentApiViewV2(generics.ListAPIView):
    permission_classes = [permissions.IsAuthenticated]
    serializer_class = AllNewsSerializerRecentView

    filter_backends = [filters.SearchFilter]
    search_fields = ['title', 'description', '=source']

    pagination_class = PageNumberPagination

    def get_queryset(self, *args, **kwargs):
        now = datetime.datetime.now()
        earlier = now - datetime.timedelta(hours=latest_hours)
        name = self.kwargs['name']
        queryset = AllNews.objects.filter(category__name=name, date__range=(earlier, now)).order_by('-pk')
        return queryset

    @method_decorator(cache_page(10))
    def get(self, request, *args, **kwargs):
        queryset = self.get_queryset()
        qs = self.filter_queryset(queryset)
        rand_item_count = len(qs)
        if rand_item_count >= 3:
            rand_values = random.sample(range(rand_item_count), 3)
        elif 3 > rand_item_count > 0:
            rand_values = random.sample(range(rand_item_count), rand_item_count)
        else:
            rand_values = []
        rand_items = []
        if rand_values:
            rand_values.sort()
            for value in rand_values:
                rand_items.append(qs[value])

        serializer = AllNewsSerializerRecentView(rand_items, many=True)
        page = self.paginate_queryset(serializer.data)
        return self.get_paginated_response(page)


class RecentApiView(generics.ListAPIView):
    permission_classes = [permissions.IsAuthenticated]
    serializer_class = AllNewsSerializerListView

    filter_backends = [filters.SearchFilter]
    search_fields = ['title', 'description', '=source']

    pagination_class = PageNumberPagination

    def get_queryset(self, *args, **kwargs):
        now = datetime.datetime.now()
        earlier = now - datetime.timedelta(hours=latest_hours)
        name = self.kwargs['name']
        queryset = AllNews.objects.filter(category__name=name, date__range=(earlier, now)).order_by('-pk')
        return queryset

    @method_decorator(cache_page(10))
    def get(self, request, *args, **kwargs):
        queryset = self.get_queryset()
        qs = self.filter_queryset(queryset)
        rand_item_count = len(qs)
        if rand_item_count >= 3:
            rand_values = random.sample(range(rand_item_count), 3)
        elif 3 > rand_item_count > 0:
            rand_values = random.sample(range(rand_item_count), rand_item_count)
        else:
            rand_values = []
        rand_items = []
        if rand_values:
            rand_values.sort()
            for value in rand_values:
                rand_items.append(qs[value])

        serializer = AllNewsSerializerListView(rand_items, many=True)
        page = self.paginate_queryset(serializer.data)
        return self.get_paginated_response(page)


class NewsApiView(generics.ListAPIView):
    permission_classes = [permissions.IsAuthenticated]

    now = datetime.datetime.now()
    earlier = now - datetime.timedelta(hours=hours)

    queryset = AllNews.objects.filter(date__range=(earlier, now)).order_by('-pk')
    serializer_class = AllNewsSerializerListView

    filter_backends = [filters.SearchFilter]
    search_fields = ['title', 'description', '=source']

    pagination_class = PageNumberPagination

    def get(self, request, *args, **kwargs):
        now = datetime.datetime.now()
        earlier = now - datetime.timedelta(hours=hours)

        queryset = AllNews.objects.filter(date__range=(earlier, now)).order_by('-pk')
        qs = self.filter_queryset(queryset)
        serializer = AllNewsSerializerListView(qs, many=True)
        page = self.paginate_queryset(serializer.data)
        return self.get_paginated_response(page)


class NewsCategoryApiView(generics.ListAPIView):
    permission_classes = [permissions.IsAuthenticated]
    serializer_class = AllNewsSerializerListView

    filter_backends = [filters.SearchFilter]
    search_fields = ['title', 'description', '=source']

    pagination_class = PageNumberPagination

    def get_queryset(self, *args, **kwargs):
        now = datetime.datetime.now()
        earlier = now - datetime.timedelta(hours=hours)

        name = self.kwargs['name']
        queryset = AllNews.objects.filter(category__name=name, date__range=(earlier, now)).order_by('-pk')

        return queryset

    @method_decorator(cache_page(60))
    def get(self, request, *args, **kwargs):
        queryset = self.get_queryset()
        qs = self.filter_queryset(queryset)
        serializer = AllNewsSerializerListView(qs, many=True)
        page = self.paginate_queryset(serializer.data)
        return self.get_paginated_response(page)


class DetailNewsApiView(APIView):
    permission_classes = (permissions.IsAuthenticated,)

    # @method_decorator(cache_page(10 * 60))
    def get(self, request, pk):
        now = datetime.datetime.now()
        earlier = now - datetime.timedelta(hours=hours)

        news = get_object_or_404(AllNews, pk=pk, date__range=(earlier, now))
        data = AllNewsSerializer(news).data
        return Response(data)


class DetailNewsApiViewV2(APIView):
    permission_classes = (permissions.IsAuthenticated,)

    # @method_decorator(cache_page(10 * 60))
    def get(self, request, pk):
        now = datetime.datetime.now()
        earlier = now - datetime.timedelta(hours=hours)

        news = get_object_or_404(AllNews, pk=pk, date__range=(earlier, now))
        data = DetailNewsSerializer(news).data
        return Response(data)



class AllCategoryApiView(generics.ListAPIView):
    permission_classes = [permissions.IsAuthenticated]

    queryset = AllNewsCategory.objects.all()
    serializer_class = AllNewsCategorySerializer
    pagination_class = PageNumberPagination

    def get(self, request, *args, **kwargs):
        queryset = AllNewsCategory.objects.all()
        serializer = AllNewsCategorySerializer(queryset, many=True)
        page = self.paginate_queryset(serializer.data)
        return self.get_paginated_response(page)



