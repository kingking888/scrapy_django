from django.db import models
from django.utils import timezone


class News(models.Model):
    title = models.TextField(unique=True)
    description = models.TextField()
    image = models.TextField()
    url = models.TextField(null=True, blank=True)
    category = models.CharField(max_length=200, blank=True, null=True)
    date = models.DateTimeField(default=timezone.now)

    class Meta:
        verbose_name_plural = "Newses"

    def __str__(self):
        return self.title