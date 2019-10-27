package com.softaholik.bdnewstoday.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.softaholik.bdnewstoday.R;
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
        newsViewModel.getAllNews().observe(this, new Observer<List<News>>() {
            @Override
            public void onChanged(@Nullable List<News> news) {
                //update RecyclerView
                Toast.makeText(mContext, "No news", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
