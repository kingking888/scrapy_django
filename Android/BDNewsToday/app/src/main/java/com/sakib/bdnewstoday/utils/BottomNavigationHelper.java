package com.sakib.bdnewstoday.utils;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;


import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.sakib.bdnewstoday.R;
import com.sakib.bdnewstoday.activities.main.HighlightActivity;
import com.sakib.bdnewstoday.activities.main.MoreActivity;
import com.sakib.bdnewstoday.activities.main.NewsActivity;

/**
 * The type Bottom navigation helper.
 */
public class BottomNavigationHelper {
    private static final String TAG = "BottomNavViewHelper";
    /**
     * The Context.
     */
    Context context;

    /**
     * Sets bottom navigation view.
     *
     * @param bottomNavigationViewEx the bottom navigation view ex
     */
    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx) {
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
        bottomNavigationViewEx.enableAnimation(true);
        bottomNavigationViewEx.enableItemShiftingMode(false);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextSize(12);
//        bottomNavigationViewEx.setTextVisibility(false);
    }


    /**
     * Badge animation
     *
     * @param context         the context
     * @param callingActivity the calling activity
     * @param view            the view
     */
/*    public static Badge addBadgeAt(Context context,BottomNavigationViewEx bnve, int position, int number) {
        // add badge
        return new QBadgeView(context)
                .setBadgeNumber(number)
                .setGravityOffset(12, 2, true)
                .bindTarget(bnve.getBottomNavigationItemView(position))
                .setOnDragStateChangedListener(new Badge.OnDragStateChangedListener() {
                    @Override
                    public void onDragStateChanged(int dragState, Badge badge, View targetView) {
                        if (Badge.OnDragStateChangedListener.STATE_SUCCEED == dragState)
                            Log.d(TAG, "onDragStateChanged: badge removed");
                        //Toast.makeText(BadgeViewActivity.this, R.string.tips_badge_removed, Toast.LENGTH_SHORT).show();
                    }
                });
    }*/
    public static void enableNavigation(final Context context, final Activity callingActivity, BottomNavigationViewEx view) {
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {

                    case R.id.one:
                        Intent intent1 = new Intent(context, HighlightActivity.class);//ACTIVITY_NUM = 0
                        intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent1);
                        callingActivity.overridePendingTransition(0, 0);


                        break;

                    case R.id.two:
                        Intent intent2 = new Intent(context, NewsActivity.class);//ACTIVITY_NUM = 1
                        intent2.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent2);

                        callingActivity.overridePendingTransition(0, 0);
                        break;

                    case R.id.three:
                        Intent intent3 = new Intent(context, MoreActivity.class);//ACTIVITY_NUM = 2
                        intent3.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        context.startActivity(intent3);

                        callingActivity.overridePendingTransition(0, 0);
                        break;
                }


                return false;
            }
        });
    }
}
