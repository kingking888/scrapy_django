package com.softaholik.bdnewstoday.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "news_table")
public class News {
    @PrimaryKey(autoGenerate = true)
    private int id;

    private String news_id;

    private String title;

    private String description;

    private String image;

    private String date;

    public News(String news_id, String title, String description, String image, String date) {
        this.news_id = news_id;
        this.title = title;
        this.description = description;
        this.image = image;
        this.date = date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNews_id() {
        return news_id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getImage() {
        return image;
    }

    public String getDate() {
        return date;
    }
}
