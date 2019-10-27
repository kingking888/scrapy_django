package com.softaholik.bdnewstoday.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.softaholik.bdnewstoday.R;
import com.softaholik.bdnewstoday.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewsDetailsActivity extends AppCompatActivity {

    private static final String TAG = "NewsDetailsActivity";
    private Context mContext = NewsDetailsActivity.this;
    private RequestQueue requestQueue;
    private TextView newsDetailsTvTitle, newsDetailsTvTime, newsDetailsTvDesc, newsDetailsTvSource, newsDetailsTvUrl;
    private ImageView newsDetailsImage;
    private ProgressBar progressBar;
    private String id, title, image, date;
    private SessionManager sessionManager;


    private static final String BASE_URL = "https://newsapi.pythonanywhere.com/api/v1/news/";
    private static final String ACCESS_TOKEN = "Token 1c69b41523332fd3e72c9194063db9238eaf9741";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        sessionManager = new SessionManager(mContext);

        if(sessionManager.loadNightModeState()){
            setTheme(R.style.DarkActionBarTheme);
        }else {
            setTheme(R.style.AppActionBarTheme);
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_details);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
            getSupportActionBar().setTitle("");
        }


//        Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        //handle action bar null pointer exception
//        //https://stackoverflow.com/questions/29786011/how-to-fix-getactionbar-method-may-produce-java-lang-nullpointerexception
//        if (getSupportActionBar() != null) {
//            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//            getSupportActionBar().setDisplayShowHomeEnabled(true);
//        }
//        //toolbar title
//        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
//        mTitle.setTextColor(Color.WHITE);
//        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(mContext,NewsActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
//            }
//        });

        initSetup();
    }
    private void initSetup(){
        progressBar = findViewById(R.id.progressBar);

        Intent intent = getIntent();
        if (intent.hasExtra("id")) {
            id = intent.getStringExtra("id");
        }
        if (intent.hasExtra("title")) {
            title = intent.getStringExtra("title");
        }
        if (intent.hasExtra("image")) {
            image = intent.getStringExtra("image");
        }
        if (intent.hasExtra("date")) {
            date = intent.getStringExtra("date");
        }


        newsDetailsImage = findViewById(R.id.news_details_iv);
        newsDetailsTvTitle = findViewById(R.id.news_details_tv_title);
        newsDetailsTvTime = findViewById(R.id.news_details_tv_time);

        newsDetailsTvDesc = findViewById(R.id.news_details_tv_desc);

        newsDetailsTvSource = findViewById(R.id.news_details_tv_source);
        newsDetailsTvUrl = findViewById(R.id.news_details_tv_url);

        Glide.with(mContext)
                .load(image)
                .thumbnail(0.25f)
                .into(newsDetailsImage);

        newsDetailsTvTitle.setText(title);
        newsDetailsTvTime.setText(date);
        loadNewsDetails(id);




    }


    public void loadNewsDetails(final String id){

        String REQUEST_URL=BASE_URL+id;

        progressBar.setVisibility(View.VISIBLE);

//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading Data...");
//        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, REQUEST_URL,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(final JSONObject  s) {
                progressBar.setVisibility(View.INVISIBLE);
//                progressDialog.dismiss();
                try {
                    final String url =s.getString("url") ;

                    String source = s.getString("source");

                    /*source = source.replace("_"," ");
                    source = source.replaceFirst(String.valueOf(source.charAt(0)), String.valueOf(source.charAt(0)).toUpperCase());*/

                    if(source.trim().equals("prothom_alo")){
                        source="প্রথম আলো";
                    }
                    if(source.trim().equals("bd_protidin")){
                        source="বাংলাদেশ প্রতিদিন";
                    }
                    if(source.trim().equals("dhaka_tribune")){
                        source="ঢাকা ট্রিবিউন";
                    }
                    if(source.trim().equals("ittefaq")){
                        source="দৈনিক ইত্তেফাক";
                    }
                    if(source.trim().equals("jago_news24")){
                        source="জাগোনিউজ২৪.কম";
                    }
                    if(source.trim().equals("jugantor")){
                        source="দৈনিক যুগান্তর";
                    }
                    if(source.trim().equals("kaler_kantho")){
                        source="কালের কন্ঠ";
                    }
                    if(source.trim().equals("ntv_bd")){
                        source="এনটিভি টিভি";
                    }
                    if(source.trim().equals("samakal")){
                        source="দৈনিক সমকাল";
                    }



                    newsDetailsTvDesc.setText(s.getString("description"));

                    newsDetailsTvSource.setText(String.format("সোর্সঃ %s", source));

                    newsDetailsTvUrl.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(mContext, NewsSourceActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("url",url);
                            mContext.startActivity(intent);
                        }
                    });


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error.getMessage());

            }
        })

        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String , String> params = new HashMap<>();
                params.put("Authorization", ACCESS_TOKEN);
                return params;
            }
        };

        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(this);
//            Build.logError("Setting a new request queue");
        }
//        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);

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
