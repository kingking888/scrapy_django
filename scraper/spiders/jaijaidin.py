# -*- coding: utf-8 -*-
import scrapy
from scraper.items import AllNewsItem
from all_news.models import Category, News


class JaijaidinSpider(scrapy.Spider):
    category = ''
    name = 'jaijaidin'
    allowed_domains = ['jaijaidinbd.com']

    start_urls = [
            'http://www.jaijaidinbd.com/todays-paper/sports',
            'http://www.jaijaidinbd.com/todays-paper/homeland',
            'http://www.jaijaidinbd.com/todays-paper/abroad',
            'http://www.jaijaidinbd.com/todays-paper/trade-commerce',
            'http://www.jaijaidinbd.com/todays-paper/entertainment',
            'http://www.jaijaidinbd.com/feature/rong-berong',
            'http://www.jaijaidinbd.com/feature/science-and-technology',
            'http://www.jaijaidinbd.com/todays-paper/editorial',
        ]

    user_agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1"

    try:
        news_db_urls = News.objects.filter(source='jaijaidin').values_list('url', flat=True)
        news_db_urls = list(news_db_urls)
        news_db_urls = [x.rsplit('/', 1)[0] for x in news_db_urls]
    except Exception as e:
        news_db_urls = []

    def parse(self, response):
        crawled_urls = response.css('#cat_parent_content_list a::attr("href")').extract()

        news_urls = [x.rsplit('/', 1)[0] for x in crawled_urls]
        unique_urls = list(set(news_urls) - set(self.news_db_urls))

        for news_url in unique_urls:
            if 'all-news' not in news_url:
                print(news_url)
                yield response.follow(news_url, callback=self.parse_news)
            else:
                pass



    def parse_news(self, response):
        print("called")

        def listToString(s):
            # initialize an empty string
            str1 = " "

            # return string
            return (str1.join(s))

        item = AllNewsItem()
        item['title'] = response.css('.headline_section h1::text').extract_first()
        description = response.css('#myText ::text').extract()
        print(description)
        description = [x.strip() + '\n\n' for x in description]
        description = listToString(description)
        item['description'] = description
        image = response.css('.dtl_img_section img::attr(src)').extract_first()
        if image:
            image = 'http://www.jaijaidinbd.com' + image
        item['image'] = image
        item['url'] = response.request.url + '/'
        item['source'] = 'jaijaidin'

        if 'sports' in response.request.url:
            self.category = 'sports'
        if 'homeland' in response.request.url:
            self.category = 'bangladesh'
        if 'politics' in response.request.url:
            self.category = 'politics'
        if 'rong-berong' in response.request.url:
            self.category = 'lifestyle'
        if 'abroad' in response.request.url:
            self.category = 'international'
        if 'trade-commerce' in response.request.url:
            self.category = 'economy'
        if 'entertainment' in response.request.url:
            self.category = 'entertainment'
        if 'science-and-technology' in response.request.url:
            self.category = 'technology'
        if 'editorial' in response.request.url:
            self.category = 'opinion'

        item['category'] = Category.objects.get(name=self.category)

        if description:
            if 'বিস্তারিত আসছে...' not in description:
                yield item
        else:
            pass
