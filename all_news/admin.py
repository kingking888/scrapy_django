from django.contrib import admin
from .models import News, Category

# Register your models here.

class NewsAdmin(admin.ModelAdmin):
    list_display = ('title', 'description', 'category', 'source')

class CategoryAdmin(admin.ModelAdmin):
    list_display = ('name',)

admin.site.register(News, NewsAdmin)
admin.site.register(Category, CategoryAdmin)