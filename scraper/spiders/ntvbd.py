# -*- coding: utf-8 -*-
import scrapy
from scraper.items import AllNewsItem
from all_news.models import Category, News


class NTVBDSpider(scrapy.Spider):
    category = ''
    name = 'ntvbd'
    allowed_domains = ['ntvbd.com']

    start_urls = [
            'https://www.ntvbd.com/bangladesh',
            'https://www.ntvbd.com/world',
            'https://www.ntvbd.com/economy',
            'https://www.ntvbd.com/sports',
            'https://www.ntvbd.com/entertainment',
            'https://www.ntvbd.com/tech',
            'https://www.ntvbd.com/opinion'
        ]

    user_agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1"

    def parse(self, response):
        for news_url in response.css('#cat_parent_content_list a ::attr("href")').extract():
            print(news_url)
            yield response.follow(news_url, callback=self.parse_news)


    def parse_news(self, response):
        self.category = ''

        def listToString(s):
            # initialize an empty string
            str1 = " "

            return (str1.join(s))

        item = AllNewsItem()

        item['title'] = response.css('h1 ::text').extract_first()
        item['description'] = listToString(response.css('.dtl_section p ::text').extract())
        item['image'] = response.css('.dtl_img_section img::attr(src)').extract_first()
        item['url'] = response.request.url
        item['source'] = 'NtvBD'

        if 'sports' in response.request.url:
            self.category = 'sports'
        if 'bangladesh' in response.request.url:
            self.category = 'bangladesh'
        if 'world' in response.request.url:
            self.category = 'international'
        if 'economy' in response.request.url:
            self.category = 'economy'
        if 'entertainment' in response.request.url:
            self.category = 'entertainment'
        if 'tech' in response.request.url:
            self.category = 'technology'
        if 'opinion' in response.request.url:
            self.category = 'opinion'


        item['category'] = Category.objects.get(name=self.category)

        yield item

