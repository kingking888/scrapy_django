package com.softaholik.bdnewstoday.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.softaholik.bdnewstoday.R;
import com.softaholik.bdnewstoday.activities.main.NewsActivity;
import com.softaholik.bdnewstoday.utils.SessionManager;

public class NewsSourceActivity extends AppCompatActivity {
    private Context mContext = NewsSourceActivity.this;
    private WebView webView;
    private ProgressBar progressBar;
    private SessionManager sessionManager;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sessionManager = new SessionManager(mContext);

        if(sessionManager.loadNightModeState()){
            setTheme(R.style.DarkActionBarTheme);
        }else {
            setTheme(R.style.AppActionBarTheme);
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_source);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }


        progressBar = findViewById(R.id.progressBar);
        webView = findViewById(R.id.news_web_view);
        progressBar.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new WebViewClient());

        Intent intent = getIntent();
        if (intent.hasExtra("url")) {
            webView.loadUrl(intent.getStringExtra("url"));
        }


        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
