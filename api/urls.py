from django.urls import path, include

from .views import HomePageApiView, NewsApiView, DetailNewsApiView, CategoryApiView


urlpatterns = [
    path('news/', NewsApiView.as_view()),
    path('homepage/', HomePageApiView.as_view()),
    path('news/<int:pk>/', DetailNewsApiView.as_view()),
    path('category/', CategoryApiView.as_view()),
]
