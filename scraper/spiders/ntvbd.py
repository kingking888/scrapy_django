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

    try:
        news_db_urls = News.objects.filter(source='ntv_bd').values_list('url', flat=True)
        news_db_urls = list(news_db_urls)
        news_db_urls = [x.rsplit('/', 1)[0] for x in news_db_urls]
    except Exception as e:
        news_db_urls = []

    def parse(self, response):
        crawled_urls = response.css('#cat_parent_content_list a ::attr("href")').extract()

        news_urls = [x.rsplit('/', 1)[0] for x in crawled_urls]
        unique_urls = list(set(news_urls) - set(self.news_db_urls))


        for news_url in unique_urls:
            if news_url == 'https://www.ntvbd.com/all-news':
                pass
            else:
                yield response.follow(news_url, callback=self.parse_news)


    def parse_news(self, response):
        self.category = ''

        def listToString(s):
            # initialize an empty string
            str1 = " "

            return (str1.join(s))

        item = AllNewsItem()

        item['title'] = response.css('h1 ::text').extract_first()
        description = response.css('.dtl_section p ::text').extract()
        description = [x.strip() + '\n\n' for x in description]
        item['description'] = listToString(description)
        item['image'] = response.css('.dtl_img_section img::attr(src)').extract_first()
        item['url'] = response.request.url + '/'
        item['source'] = 'ntv_bd'

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

