from django.core.management.base import BaseCommand, CommandError
from all_news.models import News
from datetime import datetime, timedelta

days = 5

class Command(BaseCommand):
    help = 'Delete objects older than 10 days'

    def handle(self, *args, **options):
        News.objects.filter(date__lte=datetime.now()-timedelta(days=days)).delete()
        print("Deleted items older than {} days".format(days))
