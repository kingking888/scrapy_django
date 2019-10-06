# -*- coding: utf-8 -*-
import scrapy
from scraper.items import NewsItem

class ProthomaloSpider(scrapy.Spider):
    name = 'prothomalo'
    allowed_domains = ['prothomalo.com']
    start_urls = ['https://www.prothomalo.com/sports/article?page=1']
    user_agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1"

    def parse(self, response):
        for news_url in response.css('.has_image a ::attr("href")').extract():
            yield response.follow(news_url, callback=self.parse_news)

    def parse_news(self, response):
        item = NewsItem()
        item['title'] = response.css('.mb10 ::text').extract()
        item['description'] = response.xpath('//span[@itemprop="articleBody"]/p/text()').extract()
        item['image'] = response.css('.jw_media_holder img ::src').extract()

        yield item
