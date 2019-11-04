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
* [Django 2.2.6](https://docs.djangoproject.com/en/2.2/releases/2.2.6/) - Core framework
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

<img src="https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/home_view.png" width="500" height="1000" title="Home light" />
<img src="https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/dark_mode_home.png" width="500" height="1000" title="Home dark" />

### Categorical News view
<img src="https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/news_categorical_view.png" width="500" height="1000" title="News light" />
<img src="https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/dark_mode_news.png" width="500" height="1000" title="News dark" />

### More view

<img src="https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/more_light.png" width="500" height="1000" title="More light" />
<img src="https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/more_dark.png" width="500" height="1000" title="More dark" />

### Saved news view

<img src="https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/saved_news_light.png" width="500" height="1000" title="Saved news  light" />
<img src="https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/saved_news_dark.png" width="500" height="1000" title="Saved news  dark" />

### News Details
<img src="https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/news_details.png" width="500" height="1000" title="News Details" />
<img src="https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/news_details_dark.png" width="500" height="1000" title="News Details Dark" />
<img src="https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/news_details_web.png" width="500" height="1000" title="News Details Web" />

### Skeleton view

<img src="https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/skeleton_view_light_highlight.png" width="500" height="1000" title="Skeleton View highlight light" />
<img src="https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/skeleton_view_dark_highlight.png" width="500" height="1000" title="Skeleton View Dark" />

<img src="https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/skeleton_view_light_news.png" width="500" height="1000" title="Skeleton View highlight light News" />
<img src="https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/skeleton_view_dark_news.png" width="500" height="1000" title="Skeleton View Dark News" />


### Zoom image dialog

<img src="https://github.com/tohfaakib/scrapy_django/blob/android_stable/Android/screenshots/zoom_image_dialog.png" width="500" height="1000" title="Zoom image dialog" />
