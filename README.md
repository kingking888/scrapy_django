# BDNews Today
An android application where news are displayed from most popular Bangla newspapers which are collected using this **Scrapy-Django** integration as a backend API that includes proper authentication. 

## Authors

* **TOHFA AKIB**  - [tohfaakib](https://github.com/tohfaakib)
* **SAKIB,AHMED SHAHRIAR**  - [ahmedshahriar](https://github.com/ahmedshahriar)

### Prerequisites

```
Django, Django REST API, Android
```

## Built With
#### API
* [Django 2.2.6](https://docs.djangoproject.com/en/2.2/releases/2.2.6/) - Core API framework
* [Django Rest Framework 3.10.3](https://www.django-rest-framework.org/) - API toolkit
* [Scrapy 1.7.3](https://scrapy.org/) - Web-Crawling Framework 
* [Python 3.7](https://www.python.org/downloads/release/python-370/) - Core language

#### Android app
* [Java 1.8](https://javadl.oracle.com/webapps/download/AutoDL?BundleId=240728_5b13a193868b4bf28bcb45c792fce896) - Core language
* Librares
  * [ROOM 2.2.1](https://developer.android.com/topic/libraries/architecture/room) - Database
  * [Lifecycle 2.2.1](https://developer.android.com/topic/libraries/architecture/lifecycle) - Lifecycle-aware components
  * [BottomnavigationVieEx 2.0.4](https://github.com/ittianyu/BottomNavigationViewEx) - Bottom Navigation View
  * [Volley 1.1.1](https://developer.android.com/training/volley) - HTTP library
  * [Glide 4.10.0](https://github.com/bumptech/glide) - Image loading framework
  * [Skeleton 1.1.2](https://github.com/ethanhua/Skeleton) - Skeleton loading view 

## App Features

  * Browse highlight news (landing view)
  * Search from all news in highlight  view
  * Browse categorical news
  * Filter seach by category in News view
  * Pull to refresh news / highlight view
  * View detailed News
  * Save news from news list and news details view
  * Zoom image from detailed news section
  * Change theme (light and dark)
  * Clear cache
  * View saved news in offline 
  * Offline cache enabled for highlight and news view so that user can browse those cached news in offline.
  
  
## App Screenshots
### Home View
![alt text](https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/home_view.png "Home light")
![alt text](https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/dark_mode_home.png " Home dark")

### Categorical News view
![alt text](https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/news_categorical_view.png "News light")
![alt text](https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/dark_mode_news.png "News dark")

### News Details
![alt text](https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/news_details.png "News Details")
![alt text](https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/news_details_dark.png "News Details")
![alt text](https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/news_details_web.png "News Details Web")

### Skeleton view
![alt text](https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/skeleton_view.png "Skeleton View")
![alt text](https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/skeleton_view_dark.png "Skeleton View Dark")

