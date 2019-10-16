# -*- coding: utf-8 -*-
import scrapy
from scraper.items import AllNewsItem
from all_news.models import Category, News


class IttefaqSpider(scrapy.Spider):
    category = ''
    name = 'ittefaq'
    allowed_domains = ['ittefaq.com.bd']

    start_urls = [
            'https://www.ittefaq.com.bd/all-news/national/?pg=1',
            'https://www.ittefaq.com.bd/all-news/politics/?pg=1',
            'https://www.ittefaq.com.bd/all-news/wholecountry/?pg=1',
            'https://www.ittefaq.com.bd/all-news/worldnews/?pg=1',
            'https://www.ittefaq.com.bd/all-news/sports/?pg=1',
            'https://www.ittefaq.com.bd/all-news/entertainment/?pg=1',
            'https://www.ittefaq.com.bd/all-news/economy/?pg=1',
            'https://www.ittefaq.com.bd/all-news/scienceandtechnology/?pg=1',
        ]

    user_agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1"

    try:
        news_db_urls = News.objects.filter(source='Ittefaq').values_list('url', flat=True)
        news_db_urls = list(news_db_urls)
        news_db_urls = [x.rsplit('/', 1)[0] for x in news_db_urls]
    except Exception as e:
        news_db_urls = []

    def parse(self, response):

        crawled_urls = response.css('.all_news_content_block a::attr("href")').extract()

        news_urls = [x.rsplit('/', 1)[0] for x in crawled_urls]
        unique_urls = list(set(news_urls) - set(self.news_db_urls))

        for news_url in unique_urls:
            yield response.follow(news_url, callback=self.parse_news)

    def parse_news(self, response):

        def listToString(s):
            # initialize an empty string
            str1 = " "

            # return string
            return (str1.join(s))

        item = AllNewsItem()
        item['title'] = response.css('.dtl_hl_block h1::text').extract_first()
        description = listToString(response.css('.dtl_content_block span::text').extract())
        if not description:
            description = listToString(response.css('.dtl_content_block p::text').extract())
        item['description'] = description
        item['image'] = 'https://' + self.allowed_domains[0] + response.css('.dtl_img_block img::attr(src)').extract_first()
        item['url'] = response.request.url+'/'
        item['source'] = 'Ittefaq'

        if 'sports' in response.request.url:
            self.category = 'sports'
        if 'national' in response.request.url:
            self.category = 'bangladesh'
        if 'politics' in response.request.url:
            self.category = 'politics'
        if 'wholecountry' in response.request.url:
            self.category = 'bangladesh'
        if 'worldnews' in response.request.url:
            self.category = 'international'
        if 'economy' in response.request.url:
            self.category = 'economy'
        if 'entertainment' in response.request.url:
            self.category = 'entertainment'
        if 'scienceandtechnology' in response.request.url:
            self.category = 'technology'

        item['category'] = Category.objects.get(name=self.category)

        yield item
