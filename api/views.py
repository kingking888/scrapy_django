from django.shortcuts import get_object_or_404


from rest_framework.views import APIView
from rest_framework import permissions, generics
from rest_framework.response import Response
from rest_framework.pagination import PageNumberPagination
from rest_framework import filters

import datetime
import random

from all_news.models import News as AllNews
from all_news.models import Category as AllNewsCategory

from .serializers import AllNewsSerializer
from .serializers import AllNewsCategorySerializer

hours = 24
latest_hours = 1


class RecentApiView(APIView):
    permission_classes = (permissions.IsAuthenticated, )

    def get(self, request):

        try:
            now = datetime.datetime.now()
            earlier = now - datetime.timedelta(hours=latest_hours)

            num_entities = AllNews.objects.filter(date__range=(earlier, now)).count()
            first_value = AllNews.objects.filter(date__range=(earlier, now))[:1].get().pk

            rand_entities = random.sample(range(num_entities), 20)
            rand_entities = [x + first_value-1 for x in rand_entities]

            news = AllNews.objects.filter(date__range=(earlier, now), pk__in=rand_entities).order_by('-pk')
            data = AllNewsSerializer(news, many=True).data
            return Response(data)
        except Exception as e:
            data = ''
            return Response(data)


class NewsApiView(generics.ListAPIView):
    permission_classes = [permissions.IsAuthenticated]

    now = datetime.datetime.now()
    earlier = now - datetime.timedelta(hours=hours)

    queryset = AllNews.objects.filter(date__range=(earlier, now)).order_by('-pk')
    serializer_class = AllNewsSerializer

    filter_backends = [filters.SearchFilter]
    search_fields = ['title', 'description', '=source']

    pagination_class = PageNumberPagination

    def get(self, request, *args, **kwargs):
        now = datetime.datetime.now()
        earlier = now - datetime.timedelta(hours=hours)

        queryset = AllNews.objects.filter(date__range=(earlier, now)).order_by('-pk')
        qs = self.filter_queryset(queryset)
        serializer = AllNewsSerializer(qs, many=True)
        page = self.paginate_queryset(serializer.data)
        return self.get_paginated_response(page)


class NewsCategoryApiView(generics.ListAPIView):
    permission_classes = [permissions.IsAuthenticated]
    serializer_class = AllNewsSerializer

    filter_backends = [filters.SearchFilter]
    search_fields = ['title', 'description', '=source']

    pagination_class = PageNumberPagination

    def get_queryset(self, *args, **kwargs):
        now = datetime.datetime.now()
        earlier = now - datetime.timedelta(hours=hours)

        name = self.kwargs['name']
        queryset = AllNews.objects.filter(category__name=name, date__range=(earlier, now)).order_by('-pk')

        return queryset

    def get(self, request, *args, **kwargs):
        queryset = self.get_queryset()
        qs = self.filter_queryset(queryset)
        serializer = AllNewsSerializer(qs, many=True)
        page = self.paginate_queryset(serializer.data)
        return self.get_paginated_response(page)


class DetailNewsApiView(APIView):
    permission_classes = (permissions.IsAuthenticated,)

    def get(self, request, pk):
        now = datetime.datetime.now()
        earlier = now - datetime.timedelta(hours=hours)

        news = get_object_or_404(AllNews, pk=pk, date__range=(earlier, now))
        data = AllNewsSerializer(news).data
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


# class AllCategoryApiView(APIView):
#     permission_classes = (permissions.IsAuthenticated,)
#
#     def get(self, request):
#         categories = AllNewsCategory.objects.all()
#         data = AllNewsCategorySerializer(categories, many=True).data
#         return Response(data)


