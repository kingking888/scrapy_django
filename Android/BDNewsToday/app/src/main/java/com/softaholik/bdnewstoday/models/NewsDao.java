package com.softaholik.bdnewstoday.models;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface NewsDao {

    @Insert
    void insert(News news);

    @Delete
    void delete(News news);

    @Query("DELETE FROM news_table")
    void deleteAllNotes();

    @Query("SELECT * FROM news_table ORDER BY date DESC")
    LiveData<List<News>> getAllNews();
}
