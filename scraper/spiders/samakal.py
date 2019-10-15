# -*- coding: utf-8 -*-
import scrapy
from scraper.items import AllNewsItem
from all_news.models import Category, News


class SamakalSpider(scrapy.Spider):
    category = ''
    name = 'samakal'
    allowed_domains = ['samakal.com']

    start_urls = [
            'https://samakal.com/bangladesh',
            'https://samakal.com/politics',
            'https://samakal.com/economics',
            'https://samakal.com/international',
            'https://samakal.com/sports',
            'https://samakal.com/entertainment',
            'https://samakal.com/lifestyle',
            'https://samakal.com/chakri',
            'https://samakal.com/technology',
            'https://samakal.com/comments'
        ]

    user_agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1"

    def parse(self, response):
        for news_url in response.css('.main-div a ::attr("href")').extract():
            if '?page=2' in news_url:
                pass
            else:
                # print(news_url)
                yield response.follow(news_url, callback=self.parse_news)

    def parse_news(self, response):
        self.category = ''

        def listToString(s):
            # initialize an empty string
            str1 = " "

            return (str1.join(s))

        item = AllNewsItem()

        item['title'] = response.css('.detail-headline ::text').extract_first()
        item['description'] = listToString(response.css('.description p ::text').extract())
        item['image'] = response.css('.image-container img::attr(src)').extract_first()
        item['url'] = response.request.url
        item['source'] = 'Samakal'

        if 'sports' in response.request.url:
            self.category = 'sports'
        if 'bangladesh' in response.request.url:
            self.category = 'bangladesh'
        if 'politics' in response.request.url:
            self.category = 'politics'
        if 'international' in response.request.url:
            self.category = 'international'
        if 'economics' in response.request.url:
            self.category = 'economy'
        if 'entertainment' in response.request.url:
            self.category = 'entertainment'
        if 'technology' in response.request.url:
            self.category = 'technology'
        if 'lifestyle' in response.request.url:
            self.category = 'lifestyle'
        if 'chakri' in response.request.url:
            self.category = 'job'
        if 'comments' in response.request.url:
            self.category = 'opinion'


        item['category'] = Category.objects.get(name=self.category)

        yield item

