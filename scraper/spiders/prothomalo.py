# -*- coding: utf-8 -*-
import scrapy
from scraper.items import AllNewsItem
from all_news.models import Category, News


class ProthomaloSpider(scrapy.Spider):
    category = ''
    name = 'prothomalo'
    allowed_domains = ['prothomalo.com']

    start_urls = [
            'https://www.prothomalo.com/sports/article?page=1',
            'https://www.prothomalo.com/bangladesh/article?page=1',
            'https://www.prothomalo.com/international/article?page=1',
            'https://www.prothomalo.com/economy/article?page=1',
            'https://www.prothomalo.com/entertainment/article?page=1',
            'https://www.prothomalo.com/topic/%E0%A6%9A%E0%A6%BE%E0%A6%95%E0%A6%B0%E0%A6%BF%E0%A6%AC%E0%A6%BE%E0%A6%95%E0%A6%B0%E0%A6%BF?page=1',
            'https://www.prothomalo.com/technology/article?page=1',
            'https://www.prothomalo.com/life-style/article?page=1',
            'https://www.prothomalo.com/pachmisheli/article?page=1',
            'https://www.prothomalo.com/opinion/article?page=1',
        ]

    user_agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1"

    try:
        news_db_urls = News.objects.filter(source='prothom_alo').values_list('url', flat=True)
        news_db_urls = list(news_db_urls)
    except Exception as e:
        news_db_urls = []

    def parse(self, response):
        crawled_urls = response.css('.has_image a ::attr("href")').extract()

        news_urls = ['https://www.prothomalo.com' + x for x in crawled_urls]
        news_urls = [y.replace('#comments', '') if '#comments' in y else y for y in news_urls]

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

        item['title'] = response.css('.mb10 ::text').extract_first()
        description = response.css('div[itemprop=articleBody] p ::text').extract()
        description = [x.strip()+'\n\n' if not 'প্রথম আলো' in x else x.strip('\n') for x in description]
        item['description'] = listToString(description)
        image = response.css('div[itemprop=articleBody] img::attr(src)').extract_first()
        if not image:
            image = response.css('.featured_image img::attr(src)').extract_first()
            if image:
                image = 'https:' + image
        item['image'] = image
        item['url'] = response.request.url
        item['source'] = 'prothom_alo'

        if 'sports' in response.request.url:
            self.category = 'sports'
        if 'bangladesh' in response.request.url:
            self.category = 'bangladesh'
        if 'international' in response.request.url:
            self.category = 'international'
        if 'economy' in response.request.url:
            self.category = 'economy'
        if 'entertainment' in response.request.url:
            self.category = 'entertainment'
        if 'chakri-bakri' in response.request.url:
            self.category = 'job'
        if 'technology' in response.request.url:
            self.category = 'technology'
        if 'life-style' in response.request.url:
            self.category = 'lifestyle'
        if 'pachmisheli' in response.request.url:
            self.category = 'pachmishali'
        if 'opinion' in response.request.url:
            self.category = 'opinion'

        item['category'] = Category.objects.get(name=self.category)

        yield item
