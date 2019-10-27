package com.softaholik.bdnewstoday.models.db;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.softaholik.bdnewstoday.models.News;
import com.softaholik.bdnewstoday.models.NewsDao;

@Database(entities = {News.class}, version = 1,  exportSchema = false)
public abstract class NewsDatabase extends RoomDatabase {

    private static NewsDatabase instance;

    public abstract NewsDao newsDao();

    public static synchronized NewsDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NewsDatabase.class, "news_database")
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private NewsDao newsDao;

        private PopulateDbAsyncTask(NewsDatabase db) {
            newsDao = db.newsDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
           // newsDao.insert(new News("Title 1", "Description 1", 1));
            return null;
        }
    }
}
