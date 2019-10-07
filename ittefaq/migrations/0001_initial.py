# Generated by Django 2.2.6 on 2019-10-07 13:57

from django.db import migrations, models
import django.utils.timezone


class Migration(migrations.Migration):

    initial = True

    dependencies = [
    ]

    operations = [
        migrations.CreateModel(
            name='News',
            fields=[
                ('id', models.AutoField(auto_created=True, primary_key=True, serialize=False, verbose_name='ID')),
                ('title', models.TextField(unique=True)),
                ('description', models.TextField()),
                ('image', models.TextField()),
                ('url', models.TextField(blank=True, null=True)),
                ('category', models.CharField(blank=True, max_length=200, null=True)),
                ('date', models.DateTimeField(default=django.utils.timezone.now)),
            ],
            options={
                'verbose_name_plural': 'Newses',
            },
        ),
    ]
