# -*- coding: utf-8 -*-

# Define here the models for your scraped items
#
# See documentation in:
# https://docs.scrapy.org/en/latest/topics/items.html


from scrapy_djangoitem import DjangoItem
from prothomalo.models import News as ProthomaloNews
from kalerkantho.models import News as KalerkanthoNews
from ittefaq.models import News as IttefaqNews
from bdprotidin.models import News as BdprotidinNews
from all_news.models import News as AllNews


class ProthomaloNewsItem(DjangoItem):
    django_model = ProthomaloNews


class KalerkanthoNewsItem(DjangoItem):
    django_model = KalerkanthoNews


class IttefaqNewsItem(DjangoItem):
    django_model = IttefaqNews


class BdprotidinNewsItem(DjangoItem):
    django_model = BdprotidinNews

class AllNewsItem(DjangoItem):
    django_model = AllNews
