package com.sakib.bdnewstoday.models;

public class NewsList {

    private String news_id;
    private String title;
    private String short_description;
    private String image;
    private String date;

    public NewsList() {
    }

    public NewsList(String news_id, String title, String short_description, String image, String date) {
        this.news_id = news_id;
        this.title = title;
        this.short_description = short_description;
        this.image = image;
        this.date = date;
    }

    public String getNews_id() {
        return news_id;
    }

    public String getTitle() {
        return title;
    }

    public String getShort_description() {
        return short_description;
    }

    public String getImage() {
        return image;
    }

    public String getDate() {
        return date;
    }
}
