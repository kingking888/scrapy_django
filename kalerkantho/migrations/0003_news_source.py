# Generated by Django 2.2.6 on 2019-10-08 16:59

from django.db import migrations, models


class Migration(migrations.Migration):

    dependencies = [
        ('kalerkantho', '0002_auto_20191008_1857'),
    ]

    operations = [
        migrations.AddField(
            model_name='news',
            name='source',
            field=models.CharField(blank=True, max_length=200, null=True),
        ),
    ]
