package com.softaholik.bdnewstoday.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.softaholik.bdnewstoday.activities.main.HighlightActivity;

public class SplashActivity extends AppCompatActivity {
    private static final String TAG = "MoreActivity";
    private Context mContext = SplashActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = new Intent(mContext, HighlightActivity.class);
        startActivity(intent);
        finish();
    }
}
