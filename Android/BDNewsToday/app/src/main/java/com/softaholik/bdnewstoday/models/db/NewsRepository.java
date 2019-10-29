package com.softaholik.bdnewstoday.models.db;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.softaholik.bdnewstoday.models.News;
import com.softaholik.bdnewstoday.models.NewsDao;

import java.util.List;

public class NewsRepository {
    private NewsDao newsDao;
    private LiveData<List<News>> allNews;

    public NewsRepository(Application application) {
        NewsDatabase database = NewsDatabase.getInstance(application);
        newsDao = database.newsDao();
        allNews = newsDao.getAllNews();
    }

    public void insert(News news) {
        new InsertNewsAsyncTask(newsDao).execute(news);
    }

    public void delete(News news) {
        new DeleteNoteAsyncTask(newsDao).execute(news);
    }

    public void deleteAllNews() {
        new DeleteAllNotesAsyncTask(newsDao).execute();
    }

    public LiveData<List<News>> getAllNews() {
        return allNews;
    }

    private static class InsertNewsAsyncTask extends AsyncTask<News, Void, Void> {
        private NewsDao noteDao;

        private InsertNewsAsyncTask(NewsDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(News... notes) {
            noteDao.insert(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<News, Void, Void> {
        private NewsDao newsDao;

        private DeleteNoteAsyncTask(NewsDao newsDao) {
            this.newsDao = newsDao;
        }

        @Override
        protected Void doInBackground(News... notes) {
            newsDao.delete(notes[0]);
            return null;
        }
    }

    private static class DeleteAllNotesAsyncTask extends AsyncTask<Void, Void, Void> {
        private NewsDao newsDao;

        private DeleteAllNotesAsyncTask(NewsDao newsDao) {
            this.newsDao = newsDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            newsDao.deleteAllNotes();
            return null;
        }
    }
}
