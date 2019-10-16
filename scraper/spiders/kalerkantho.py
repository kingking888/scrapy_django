# -*- coding: utf-8 -*-
import scrapy
from scraper.items import AllNewsItem
from all_news.models import Category, News
from django.db import IntegrityError
from django.shortcuts import get_object_or_404

class KalerkanthoSpider(scrapy.Spider):
    category = ''
    name = 'kalerkantho'
    allowed_domains = ['kalerkantho.com']

    start_urls = [
            'https://www.kalerkantho.com/online/country_news/1',
            'https://www.kalerkantho.com/online/national/1',
            'https://www.kalerkantho.com/online/world/1',
            'https://www.kalerkantho.com/online/business/1',
            'https://www.kalerkantho.com/online/entertainment/1',
            'https://www.kalerkantho.com/online/sport/1',
            'https://www.kalerkantho.com/online/lifestyle/1',
            'https://www.kalerkantho.com/online/info-tech/1',
            'https://www.kalerkantho.com/online/miscellaneous/1'
        ]

    user_agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1"

    news_db_urls = News.objects.filter(source='Kaler Kantho').values_list('url', flat=True)
    news_db_urls = list(news_db_urls)

    def parse(self, response):
        crawled_urls = response.css('.n_row a::attr("href")').extract()
        news_urls = [x.replace('.', 'https://www.kalerkantho.com') for x in crawled_urls]

        unique_urls = list(set(news_urls) - set(self.news_db_urls))

        print(unique_urls)

        for news_url in unique_urls:
            yield response.follow(news_url, callback=self.parse_news)

    def parse_news(self, response):

        def listToString(s):
            # initialize an empty string
            str1 = " "

            # return string
            return (str1.join(s))

        item = AllNewsItem()
        item['title'] = response.css('h2::text').extract_first()
        item['description'] = listToString(response.css('.some-class-name2 p ::text').extract())
        item['image'] = response.css('.img-popup img::attr(src)').extract_first()
        item['url'] = response.request.url
        item['source'] = 'Kaler Kantho'

        if 'sport' in response.request.url:
            self.category = 'sports'
        if 'country-news' in response.request.url:
            self.category = 'bangladesh'
        if 'national' in response.request.url:
            self.category = 'bangladesh'
        if 'world' in response.request.url:
            self.category = 'international'
        if 'business' in response.request.url:
            self.category = 'economy'
        if 'entertainment' in response.request.url:
            self.category = 'entertainment'
        if 'info-tech' in response.request.url:
            self.category = 'technology'
        if 'lifestyle' in response.request.url:
            self.category = 'lifestyle'
        if 'miscellaneous' in response.request.url:
            self.category = 'pachmishali'

        item['category'] = Category.objects.get(name=self.category)

        yield item


















# , meta = {'category': self.category}
# item['category'] = response.meta.get('category')
