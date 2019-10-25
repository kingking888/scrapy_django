# -*- coding: utf-8 -*-
import re
import scrapy
from scraper.items import AllNewsItem
from all_news.models import Category, News


class BdprotidinSpider(scrapy.Spider):
    category = ''
    name = 'bdprotidin'
    allowed_domains = ['bd-pratidin.com']

    start_urls = [
            'https://www.bd-pratidin.com/national/1',
            'https://www.bd-pratidin.com/country/1',
            'https://www.bd-pratidin.com/city-news/1',
            'https://www.bd-pratidin.com/international-news/1',
            'https://www.bd-pratidin.com/entertainment/1',
            'https://www.bd-pratidin.com/sports/1',
            'https://www.bd-pratidin.com/tech-world/1',
            'https://www.bd-pratidin.com/mixter/1',
            'https://www.bd-pratidin.com/job-market/1',
            'https://www.bd-pratidin.com/life/1',

        ]

    user_agent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.1 (KHTML, like Gecko) Chrome/22.0.1207.1 Safari/537.1"

    try:
        news_db_urls = News.objects.filter(source='bd_protidin').values_list('url', flat=True)
        news_db_urls = list(news_db_urls)
    except Exception as e:
        news_db_urls = []

    def parse(self, response):
        crawled_urls = response.css('.lead-news-3nd a::attr("href")').extract()
        news_urls = ['https://www.bd-pratidin.com/' + x for x in crawled_urls]

        unique_urls = list(set(news_urls) - set(self.news_db_urls))

        for news_url in unique_urls:
            yield response.follow(news_url, callback=self.parse_news)

    def parse_news(self, response):
        TAG_RE = re.compile(r'<[^>]+>')

        def remove_tags(text):
            return TAG_RE.sub('', text)

        item = AllNewsItem()

        item['title'] = response.css('.post-title ::text').extract_first()
        item['description'] = remove_tags(response.xpath("//article").extract_first()).replace("googletag.cmd.push(function() { googletag.display('div-gpt-ad-1551006634778-0'); });", "").strip()
        item['image'] = 'https://www.' + self.allowed_domains[0] + '/' + response.css('.main-image img::attr(src)').extract_first()
        item['url'] = response.request.url
        item['source'] = 'bd_protidin'

        if 'sports' in response.request.url:
            self.category = 'sports'
        if '/national/' in response.request.url:
            self.category = 'bangladesh'
        if 'country' in response.request.url:
            self.category = 'bangladesh'
        if 'city-news' in response.request.url:
            self.category = 'bangladesh'
        if 'international-news' in response.request.url:
            self.category = 'international'
        if 'mixter' in response.request.url:
            self.category = 'pachmishali'
        if 'entertainment' in response.request.url:
            self.category = 'entertainment'
        if 'tech-world' in response.request.url:
            self.category = 'technology'
        if 'life' in response.request.url:
            self.category = 'lifestyle'
        if 'job-market' in response.request.url:
            self.category = 'job'

        item['category'] = Category.objects.get(name=self.category)

        yield item
