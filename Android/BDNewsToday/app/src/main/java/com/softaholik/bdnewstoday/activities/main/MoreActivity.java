package com.softaholik.bdnewstoday.activities.main;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.softaholik.bdnewstoday.BuildConfig;
import com.softaholik.bdnewstoday.R;
import com.softaholik.bdnewstoday.activities.SavedNewsActivity;
import com.softaholik.bdnewstoday.utils.BottomNavigationHelper;
import com.softaholik.bdnewstoday.utils.SessionManager;

public class MoreActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "MoreActivity";
    private static final int ACTIVITY_NUM = 2;
    private Context mContext = MoreActivity.this;

    private ConstraintLayout moreClSavedNews, moreClAbout, moreClTheme, moreClShare, moreClRate;
    private Switch themeSwitch;
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
        setContentView(R.layout.activity_more);
        setupBottomNavigationView();
        initSetup();
    }

    private void initSetup() {
        moreClTheme = findViewById(R.id.more_cl_theme);
        moreClSavedNews = findViewById(R.id.more_cl_saved_news);
        moreClShare = findViewById(R.id.more_cl_share);
        moreClRate = findViewById(R.id.more_cl_rate);
        moreClAbout = findViewById(R.id.more_cl_about);


        themeSwitch = findViewById(R.id.theme_switch);

        moreClTheme.setOnClickListener(this);
        moreClSavedNews.setOnClickListener(this);
        moreClShare.setOnClickListener(this);
        moreClRate.setOnClickListener(this);
        moreClAbout.setOnClickListener(this);

        if(sessionManager.loadNightModeState()){
            themeSwitch.setChecked(true);
        }else {
            themeSwitch.setChecked(false);
        }

        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    sessionManager.setNightMode(true);
                    restartApp();
                }else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    sessionManager.setNightMode(false);
                    restartApp();
                }
            }
        });

    }

    private void restartApp(){
        Intent intent = new Intent(getApplicationContext(),MoreActivity.class);
        startActivity(intent);
        finish();
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

    /**
     * Start with rating the app
     * Determine if the Play Store is installed on the device
     */
    public void rateApp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection_deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_cl_saved_news:
                Intent savedNewsIntent = new Intent(mContext, SavedNewsActivity.class);
                startActivity(savedNewsIntent);
                break;
            case R.id.more_cl_theme:

                break;
            case R.id.more_cl_about:
                showAlertBox();
                break;
            case R.id.more_cl_rate:
                rateApp();
                break;
            case R.id.more_cl_share:
                try {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    shareIntent.setType("text/plain");
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, "BDNewsToday");
                    String shareMessage = "Check out BDNewsToday!\n";
                    shareMessage = shareMessage + "Get the latest news from most popular Bangla newspapers in one app on your Android device.Get it for free at " +
                            " https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
                    shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                    startActivity(Intent.createChooser(shareIntent, "Share BDNewsToday via..."));
                } catch (Exception e) {
                    //e.toString();
                }
                break;
            default:
                break;
        }
    }
}
