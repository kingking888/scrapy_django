# -*- coding: utf-8 -*-

# Define your item pipelines here
#
# Don't forget to add your pipeline to the ITEM_PIPELINES setting
# See: https://docs.scrapy.org/en/latest/topics/item-pipeline.html

from django.db import IntegrityError


class ScraperPipeline(object):
    def process_item(self, item, spider):
        try:
            item.save()
            return item
        except IntegrityError as e:
            if 'UNIQUE constraint' in str(e.args):
                pass



