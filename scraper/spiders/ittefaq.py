# -*- coding: utf-8 -*-
import scrapy
from scraper.items import IttefaqNewsItem


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

    def parse(self, response):
        for news_url in response.css('.all_news_content_block a::attr("href")').extract():

            # print("crawled news: "+news_url)

            yield response.follow(news_url, callback=self.parse_news)


    def parse_news(self, response):

        def listToString(s):
            # initialize an empty string
            str1 = " "

            # return string
            return (str1.join(s))

        item = IttefaqNewsItem()
        item['title'] = response.css('.dtl_hl_block h1::text').extract_first()
        description = listToString(response.css('.dtl_content_block span::text').extract())
        if not description:
            description = listToString(response.css('.dtl_content_block p::text').extract())
        item['description'] = description
        item['image'] = 'https://' + self.allowed_domains[0] + response.css('.dtl_img_block img::attr(src)').extract_first()
        item['url'] = response.request.url

        if 'sports' in response.request.url:
            self.category = 'sports'
        if 'national' in response.request.url:
            self.category = 'bangladesh'
        if 'politics' in response.request.url:
            self.category = 'bangladesh'
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

        item['category'] = self.category

        yield item
