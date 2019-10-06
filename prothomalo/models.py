from django.db import models

# Create your models here.

class Category(models.Model):
    name = models.CharField(max_length=255)

    class Meta:
        verbose_name = "Categorie"

    def __str__(self):
        return self.name


class News(models.Model):
    title = models.TextField()
    description = models.TextField()
    image = models.TextField()
    category = models.ForeignKey(Category, blank=True, null=True, on_delete=models.CASCADE)

    class Meta:
        verbose_name = "Newse"

    def __str__(self):
        return self.title