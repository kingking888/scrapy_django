# -*- coding: utf-8 -*-
import scrapy
from scraper.items import AllNewsItem
from all_news.models import Category, News


class JugantorSpider(scrapy.Spider):
    category = ''
    name = 'jugantor'
    allowed_domains = ['jugantor.com']

    start_urls = [
            'https://www.jugantor.com/economics',
            'https://www.jugantor.com/national',
            'https://www.jugantor.com/politics',
            'https://www.jugantor.com/international',
            'https://www.jugantor.com/country-news',
            'https://www.jugantor.com/sports',
            'https://www.jugantor.com/tech',
            'https://www.jugantor.com/lifestyle',
            'https://www.jugantor.com/entertainment',
            'https://www.jugantor.com/editorial'
        ]

    user_agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1"

    try:
        news_db_urls = News.objects.filter(source='jugantor').values_list('url', flat=True)
        news_db_urls = list(news_db_urls)
        news_db_urls = [x.rsplit('/', 1)[0] for x in news_db_urls]
    except Exception as e:
        news_db_urls = []

    def parse(self, response):
        crawled_urls = list(dict.fromkeys(response.css('.home_page_left a ::attr("href")').extract()))

        news_urls = [x.rsplit('/', 1)[0] for x in crawled_urls]
        unique_urls = list(set(news_urls) - set(self.news_db_urls))

        for news_url in unique_urls:
            if 'economics' in news_url or '/national' in news_url or 'editorial' in news_url or 'politics' in news_url or 'international' in news_url or 'country-news' in news_url or 'sports' in news_url or 'tech' in news_url or 'lifestyle' in news_url or 'entertainment' in news_url:
                # print(news_url)
                yield response.follow(news_url, callback=self.parse_news)
            else:
                pass



    def parse_news(self, response):
        self.category = ''

        def listToString(s):
            # initialize an empty string
            str1 = " "

            # return string
            return (str1.join(s))

        item = AllNewsItem()

        item['title'] = response.css('.headline_section ::text').extract_first()
        item['description'] = listToString(response.css('#myText p ::text').extract())
        try:
            item['image'] = 'https://www.jugantor.com' + response.css('.dtl_img_section img::attr(src)').extract_first()
        except Exception as e:
            item['image'] = ''
        item['url'] = response.request.url + '/'
        item['source'] = 'jugantor'

        if 'sports' in response.request.url:
            self.category = 'sports'
        if '/national' in response.request.url:
            self.category = 'bangladesh'
        if 'country-news' in response.request.url:
            self.category = 'bangladesh'
        if 'politics' in response.request.url:
            self.category = 'politics'
        if 'international' in response.request.url:
            self.category = 'international'
        if 'economics' in response.request.url:
            self.category = 'economy'
        if 'entertainment' in response.request.url:
            self.category = 'entertainment'
        if 'tech' in response.request.url:
            self.category = 'technology'
        if 'editorial' in response.request.url:
            self.category = 'opinion'
        if 'lifestyle' in response.request.url:
            self.category = 'lifestyle'


        item['category'] = Category.objects.get(name=self.category)

        yield item

