package com.sakib.bdnewstoday.activities.main;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.sakib.bdnewstoday.R;
import com.sakib.bdnewstoday.utils.BottomNavigationHelper;

public class MoreActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MoreActivity";
    private static final int ACTIVITY_NUM = 2;
    private Context mContext = MoreActivity.this;

    private ConstraintLayout moreClSavedNews, moreClAbout, moreClTheme;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more);
        setupBottomNavigationView();
        initSetup();
    }
    private void initSetup(){
        moreClTheme = findViewById(R.id.more_cl_theme);
        moreClSavedNews = findViewById(R.id.more_cl_saved_news);
        moreClAbout = findViewById(R.id.more_cl_about);


        moreClTheme.setOnClickListener(this);
        moreClSavedNews.setOnClickListener(this);
        moreClAbout.setOnClickListener(this);

    }


    public void showAlertBox() {
        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.about);
        dialog.setCanceledOnTouchOutside(false);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        }


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.96);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);

        dialog.getWindow().setLayout(width, height);
        ImageView cross = dialog.findViewById(R.id.terms_img_cross);

        cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //code the functionality when the cross  button is clicked
                dialog.dismiss();
            }
        });


        dialog.show();
    }

    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNav);
        BottomNavigationHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationHelper.enableNavigation(mContext, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }

    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.more_cl_saved_news:

                    break;
                case R.id.more_cl_theme:

                    break;
                case R.id.more_cl_about:
                    showAlertBox();
                    break;
            }
    }
}
