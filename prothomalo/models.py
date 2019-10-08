from django.db import models
from django.utils import timezone

# Create your models here.


class Category(models.Model):
    name = models.CharField(max_length=255)

    class Meta:
        verbose_name_plural = "Categories"

    def __str__(self):
        return self.name


class News(models.Model):
    title = models.TextField(unique=True)
    description = models.TextField()
    image = models.TextField()
    url = models.TextField(null=True, blank=True)
    # category = models.CharField(max_length=200, blank=True, null=True)
    category = models.ForeignKey('Category', blank=True, null=True, on_delete=models.CASCADE)
    source = models.CharField(max_length=200, null=True, blank=True)
    date = models.DateTimeField(default=timezone.now)

    class Meta:
        verbose_name_plural = "Newses"

    def __str__(self):
        return self.title