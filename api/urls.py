from django.urls import path, include

from .views import AllNewsAPIView, AllNewsCategoryAPIView, HomePageApiView, HomePageApiViewRealTime, AllNewsApiViewRealtime, DetailNewsApiViewRealtime

urlpatterns = [
    path('news/', AllNewsAPIView.as_view()),
    path('category/', AllNewsCategoryAPIView.as_view()),
    path('homepage/', HomePageApiView.as_view(), name='homeapi'),

    path('realtimenews/', AllNewsApiViewRealtime.as_view()),
    path('realtimehomepage/', HomePageApiViewRealTime.as_view()),
    path('realtimenews/<int:pk>/', DetailNewsApiViewRealtime.as_view()),
]
