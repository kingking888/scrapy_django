package com.softaholik.bdnewstoday.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.softaholik.bdnewstoday.R;
import com.softaholik.bdnewstoday.adapters.SavedNewsAdapter;
import com.softaholik.bdnewstoday.models.News;
import com.softaholik.bdnewstoday.models.NewsViewModel;
import com.softaholik.bdnewstoday.utils.SessionManager;

import java.util.List;

public class SavedNewsActivity extends AppCompatActivity {
    private static final String TAG = "SavedNewsActivity";
    private Context mContext = SavedNewsActivity.this;
    private NewsViewModel newsViewModel;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sessionManager = new SessionManager(mContext);

        if(sessionManager.loadNightModeState()){
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.AppTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_news);
        newsViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);

        /*News news = new News("22", "ffg", "fdgf","sdf","sd");
        newsViewModel.insert(news);*/


        RecyclerView recyclerView = findViewById(R.id.recyclerViewSavedNews);
        recyclerView.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerView.setHasFixedSize(true);

        final SavedNewsAdapter adapter = new SavedNewsAdapter(mContext);
        recyclerView.setAdapter(adapter);



        newsViewModel.getAllNews().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> news) {

                adapter.setNews(news);
                Log.d(TAG, "onChanged: "+news);
                //update RecyclerView
               // Toast.makeText(mContext, "No news", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
