# -*- coding: utf-8 -*-
import scrapy
from scraper.items import AllNewsItem
from all_news.models import Category, News


class JagoNews24Spider(scrapy.Spider):
    category = ''
    name = 'jagonews24'
    allowed_domains = ['jagonews24.com']

    start_urls = [
            'https://www.jagonews24.com/national',
            'https://www.jagonews24.com/country',
            'https://www.jagonews24.com/politics',
            'https://www.jagonews24.com/economy',
            'https://www.jagonews24.com/international',
            'https://www.jagonews24.com/sports',
            'https://www.jagonews24.com/entertainment',
            'https://www.jagonews24.com/lifestyle',
            'https://www.jagonews24.com/technology',
            'https://www.jagonews24.com/jago-jobs',
            'https://www.jagonews24.com/opinion',
        ]

    user_agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1"

    try:
        news_db_urls = News.objects.filter(source='JagoNews24').values_list('url', flat=True)
        news_db_urls = list(news_db_urls)
    except Exception as e:
        news_db_urls = []

    def parse(self, response):
        crawled_urls = list(dict.fromkeys(response.css('#loadMoreContent a ::attr("href")').extract()))

        unique_urls = list(set(crawled_urls) - set(self.news_db_urls))

        for news_url in unique_urls:
            yield response.follow(news_url, callback=self.parse_news)

    def parse_news(self, response):
        self.category = ''

        def listToString(s):
            # initialize an empty string
            str1 = " "

            return (str1.join(s))

        item = AllNewsItem()

        item['title'] = response.css('h1 ::text').extract_first()
        item['description'] = listToString(response.css('.content-details p ::text').extract())
        item['image'] = response.xpath("//meta[@name='twitter:image']/@content")[0].extract()
        item['url'] = response.request.url
        item['source'] = 'JagoNews24'

        if 'sports' in response.request.url:
            self.category = 'sports'
        if '/national' in response.request.url:
            self.category = 'bangladesh'
        if 'country' in response.request.url:
            self.category = 'bangladesh'
        if 'politics' in response.request.url:
            self.category = 'politics'
        if 'international' in response.request.url:
            self.category = 'international'
        if 'economy' in response.request.url:
            self.category = 'economy'
        if 'entertainment' in response.request.url:
            self.category = 'entertainment'
        if 'technology' in response.request.url:
            self.category = 'technology'
        if 'jago-jobs' in response.request.url:
            self.category = 'job'
        if 'lifestyle' in response.request.url:
            self.category = 'lifestyle'
        if 'opinion' in response.request.url:
            self.category = 'opinion'


        item['category'] = Category.objects.get(name=self.category)

        yield item

