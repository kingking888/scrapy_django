# -*- coding: utf-8 -*-
import scrapy
from scraper.items import AllNewsItem
from all_news.models import Category, News


class DhakaTribuneSpider(scrapy.Spider):
    category = ''
    name = 'dhakatribune'
    allowed_domains = ['bangla.dhakatribune.com']

    start_urls = [
            'https://bangla.dhakatribune.com/articles/bangladesh/page/1',
            'https://bangla.dhakatribune.com/articles/politics/page/1',
            'https://bangla.dhakatribune.com/articles/international/page/1',
            'https://bangla.dhakatribune.com/articles/economy/page/1',
            'https://bangla.dhakatribune.com/articles/sports/page/1',
            'https://bangla.dhakatribune.com/articles/entertainment/page/1',
            'https://bangla.dhakatribune.com/articles/tech/page/1',
            'https://bangla.dhakatribune.com/articles/features/page/1',
            'https://bangla.dhakatribune.com/articles/opinion/page/1'
        ]

    user_agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1"

    def parse(self, response):
        for news_url in response.css('.listing-page-news div div div a ::attr("href")').extract():
            if news_url == '/articles/bangladesh':
                pass
            else:
                # print(news_url)
                yield response.follow(news_url, callback=self.parse_news)

    def parse_news(self, response):

        def listToString(s):
            # initialize an empty string
            str1 = " "

            # return string
            return (str1.join(s))

        item = AllNewsItem()

        item['title'] = response.css('h1::text').extract_first()
        desc = listToString(response.css('.report-content p ::text').extract())
        highlighted = response.css('.highlighted-content ::text').extract_first()
        if highlighted:
            desc = desc.replace(highlighted, highlighted+'। &npsp;')
        item['description'] = desc
        item['image'] = response.css('.reports-big-img img::attr(src)').extract_first()
        item['url'] = response.request.url
        item['source'] = 'Dhaka Tribune'

        if 'sports' in response.request.url:
            self.category = 'sports'
        if 'bangladesh' in response.request.url:
            self.category = 'bangladesh'
        if 'politics' in response.request.url:
            self.category = 'bangladesh'
        if 'international' in response.request.url:
            self.category = 'international'
        if 'economy' in response.request.url:
            self.category = 'economy'
        if 'entertainment' in response.request.url:
            self.category = 'entertainment'
        if 'tech' in response.request.url:
            self.category = 'technology'
        if 'opinion' in response.request.url:
            self.category = 'opinion'
        if 'features' in response.request.url:
            self.category = 'pachmishali'

        item['category'] = Category.objects.get(name=self.category)



        yield item


















# , meta = {'category': self.category}
# item['category'] = response.meta.get('category')