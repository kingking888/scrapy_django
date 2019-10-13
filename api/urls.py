from django.urls import path, include

from .views import RecentApiView, NewsApiView, DetailNewsApiView, AllCategoryApiView, NewsCategoryApiView


urlpatterns = [
    path('news/', NewsApiView.as_view(), name='all_news'),
    path('news/category/<str:name>/', NewsCategoryApiView.as_view(), name='single_category'),
    path('news/recent/', RecentApiView.as_view(), name='recent'),
    path('news/<int:pk>/', DetailNewsApiView.as_view(), name="news_detail"),
    path('news/category/', AllCategoryApiView.as_view(), name='all_category'),
]
