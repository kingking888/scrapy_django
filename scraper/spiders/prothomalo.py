# -*- coding: utf-8 -*-
import scrapy
from scraper.items import NewsItem

class ProthomaloSpider(scrapy.Spider):
    name = 'prothomalo'
    allowed_domains = ['prothomalo.com']

    start_urls = [
            'https://www.prothomalo.com/sports/article?page=1',
            'https://www.prothomalo.com/bangladesh/article?page=1',
            'https://www.prothomalo.com/international/article?page=1',
            'https://www.prothomalo.com/economy/article?page=1',
            'https://www.prothomalo.com/entertainment/article?page=1',
            'https://www.prothomalo.com/topic/%E0%A6%9A%E0%A6%BE%E0%A6%95%E0%A6%B0%E0%A6%BF%E0%A6%AC%E0%A6%BE%E0%A6%95%E0%A6%B0%E0%A6%BF?page=1'
        ]

    user_agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1"

    def parse(self, response):
        for news_url in response.css('.has_image a ::attr("href")').extract():
            yield response.follow(news_url, callback=self.parse_news)

    def parse_news(self, response):

        def listToString(s):
            # initialize an empty string
            str1 = " "

            # return string
            return (str1.join(s))

        item = NewsItem()
        item['title'] = response.css('.mb10 ::text').extract_first()
        item['description'] = listToString(response.css('div[itemprop=articleBody] p ::text').extract())
        item['image'] = response.css('div[itemprop=articleBody] img::attr(src)').extract_first()

        # item, created = NewsItem.objects.get_or_create(title=response.css('.mb10 ::text').extract_first(),
        #                                                description=listToString(response.css('div[itemprop=articleBody] p ::text').extract()),
        #                                                image=response.css('div[itemprop=articleBody] img::attr(src)').extract_first())

        yield item
