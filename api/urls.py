from django.urls import path, include

from .views import RecentApiView, RecentApiViewV2, NewsApiView,\
    DetailNewsApiView, DetailNewsApiViewV2, AllCategoryApiView, NewsCategoryApiView


urlpatterns = [
    path('v1/news/', NewsApiView.as_view(), name='all_news'),
    path('v1/news/category/<str:name>/', NewsCategoryApiView.as_view(), name='single_category'),
    path('v1/news/recent/<str:name>/', RecentApiView.as_view(), name='recent'),
    path('v2/news/recent/<str:name>/', RecentApiViewV2.as_view(), name='recentV2'),
    path('v1/news/<int:pk>/', DetailNewsApiView.as_view(), name="news_detail"),
    path('v2/news/<int:pk>/', DetailNewsApiViewV2.as_view(), name="news_detailV2"),
    path('v1/news/category/', AllCategoryApiView.as_view(), name='all_category'),
]
