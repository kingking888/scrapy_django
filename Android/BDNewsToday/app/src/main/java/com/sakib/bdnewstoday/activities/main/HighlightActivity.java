package com.sakib.bdnewstoday.activities.main;

import android.content.Context;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.sakib.bdnewstoday.R;
import com.sakib.bdnewstoday.adapters.NewsAdapter;
import com.sakib.bdnewstoday.models.NewsList;
import com.sakib.bdnewstoday.utils.BottomNavigationHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HighlightActivity extends AppCompatActivity {

    private static final String TAG = "HighlightActivity";
    private static final int ACTIVITY_NUM = 0;


    private ProgressBar progressBar;
    private Context mContext = HighlightActivity.this;
    private RecyclerView recyclerViewSearch;

    private static final String BASE_URL = "https://newsapi.pythonanywhere.com/api/v1/";
    private static final String URL_NEWS ="news/";
    private static final String URL_NEWS_RECENT ="news/recent/";
    private static final String URL_NEWS_SEARCH="?search=";
    private static final String ACCESS_TOKEN = "Token 1c69b41523332fd3e72c9194063db9238eaf9741";


    private NewsAdapter newsAdapter;
    private SearchView searchView;
    private TextView emptyView;
    private LinearLayoutManager manager;
    private List<NewsList> newsLists;
    private RequestQueue requestQueue;
    private String next;


    //private RecyclerView.Adapter adapter;
    //use this instead

    //for scroll view
    private boolean isScrolling = false , isNewsData = false ;
    private int currentItems ,totalItems , scrollOutItems ,page_count=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highlight);

        setupBottomNavigationView();
        initSetup();


    }
    private void initSetup(){
        recyclerViewSearch = findViewById(R.id.recycler_view_search);
        emptyView = findViewById(R.id.empty_view);
        progressBar = findViewById(R.id.progressBar);
        searchView = findViewById(R.id.searchViewHighlight);

        recyclerViewSearch.setHasFixedSize(true);
        manager =new LinearLayoutManager(mContext);
        recyclerViewSearch.setLayoutManager(manager);
        newsLists = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsLists,mContext);

        recyclerViewSearch.setAdapter(newsAdapter);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
//                if (adapter != null){
//                    adapter.getFilter().filter(s);
//
//                }
//                if(medicineLists.isEmpty()){
//                    Log.d(TAG, "onQueryTextChange: no result");
//                    recyclerView.setVisibility(View.INVISIBLE);
//                    emptyView.setVisibility(View.VISIBLE);
//                }
//                if(!medicineLists.isEmpty()){
//
//                    recyclerView.setVisibility(View.VISIBLE);
//                    emptyView.setVisibility(View.INVISIBLE);
//                }

                filter(s);

                return false;
            }
            private void filter(String s) {

                if(s.trim().equals("")){
                    recyclerViewSearch.setVisibility(View.GONE);
                }else{
                loadSearchNewsData(s.trim());
                }



            }
        });

        recyclerViewSearch.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState== AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isScrolling = true ;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//               currentItems = manager.getChildCount();
//               totalItems = manager.getItemCount();
//               scrollOutItems = manager.findFirstCompletelyVisibleItemPosition();
//
//               if(dy>0){
//               if(isScrolling && (currentItems+scrollOutItems==totalItems) && isMedData && page_count<=10){
//
//
//                   Log.d(TAG, "onScrolled: done"+page_count);
//                   isScrolling = false;
//                   page_count++;
//                   new Handler().post(new Runnable() {
//                       @Override
//                       public void run() {
//                           loadMedData(page_count);
//                       }
//                   });
//
//
//               }
//               if(page_count>10) {
//                   Toast.makeText(mContext,"No more data available",Toast.LENGTH_SHORT).show();
//               }
//
//               }

                LinearLayoutManager layoutManager= (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItemCount = 0;
                int lastVisible = 0;
                if (layoutManager != null) {
                    totalItemCount = layoutManager.getItemCount();
                    lastVisible = layoutManager.findLastVisibleItemPosition();
                }

                boolean endHasBeenReached = lastVisible + 5 >= totalItemCount;
                if (isScrolling && totalItemCount > 0 && endHasBeenReached && isNewsData) {
                    //you have reached to the bottom of your recycler view
                    Log.d(TAG, "onScrolled: last scrolled"+page_count);
                    isScrolling = false;
                    page_count++;
                    new Handler().post(new Runnable() {
                        @Override
                        public void run() {
                            if(!next.equals("null")){
                                loadScrollNewsView();
                            }else{
                                Log.d(TAG, "run: no search results");
                            }

                            //loadMedData(page_count);
                        }
                    });


                }
            }
        });

    }

    /*public void loadNewsData(final String category){

        isMedData = true;

        if (!newsLists.isEmpty()){
            newsLists.clear();
        }
        newsAdapter.notifyDataSetChanged();

        //String REQUEST_URL=BASE_URL+URL_MEDICINE_DATA_PAGINATION+page_count;

        String REQUEST_URL=null;

        if(category.equals("recent")){
            REQUEST_URL=BASE_URL +URL_NEWS+category;
        }else{
            REQUEST_URL=BASE_URL+ URL_NEWS_CATEGORY +category;
        }

        progressBar.setVisibility(View.VISIBLE);

//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading Data...");
//        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, REQUEST_URL,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject  s) {
                progressBar.setVisibility(View.INVISIBLE);
//                progressDialog.dismiss();
                try {
                    next = s.getString("next");

                    JSONArray jsonArray = s.getJSONArray("results");


                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject o=jsonArray.getJSONObject(i);
                        NewsList list = new NewsList(
                                o.getString("id"),
                                o.getString("title"),
                                o.getString("short_description"),
                                o.getString("image"),
                                o.getString("date"));
                        newsLists.add(list);
                    }
                    newsAdapter = new NewsAdapter(newsLists,getApplicationContext());
                    recyclerView.setAdapter(newsAdapter);
                    newsAdapter.notifyDataSetChanged();

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

    }*/


    public void loadSearchNewsData(final String search){

        isNewsData = true;

        if (!newsLists.isEmpty()){
            newsLists.clear();
        }
        newsAdapter.notifyDataSetChanged();

        //String REQUEST_URL=BASE_URL+URL_MEDICINE_DATA_PAGINATION+page_count;

        String REQUEST_URL=BASE_URL+URL_NEWS+URL_NEWS_SEARCH+search;

        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "loadSearchNewsData: "+REQUEST_URL);
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading Data...");
//        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, REQUEST_URL,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject  s) {
                progressBar.setVisibility(View.INVISIBLE);
//                progressDialog.dismiss();
                try {
                    next = s.getString("next");
                    JSONArray jsonArray = s.getJSONArray("results");


                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject o=jsonArray.getJSONObject(i);
                        NewsList list = new NewsList(
                                o.getString("id"),
                                o.getString("title"),
                                o.getString("short_description"),
                                o.getString("image"),
                                o.getString("date"));
                        newsLists.add(list);
                    }
                    newsAdapter = new NewsAdapter(newsLists,getApplicationContext());
                    recyclerViewSearch.setAdapter(newsAdapter);
                    newsAdapter.notifyDataSetChanged();

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

    public void loadScrollNewsView(){

        isNewsData = true;


        newsAdapter.notifyDataSetChanged();

        //String REQUEST_URL=BASE_URL+URL_MEDICINE_DATA_PAGINATION+page_count;

        String REQUEST_URL=next;

        progressBar.setVisibility(View.VISIBLE);

//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading Data...");
//        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, REQUEST_URL,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject  s) {
                progressBar.setVisibility(View.INVISIBLE);
//                progressDialog.dismiss();
                try {
                    next = s.getString("next");
                    Log.d(TAG, "onResponse: "+next);
                    JSONArray jsonArray = s.getJSONArray("results");


                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject o=jsonArray.getJSONObject(i);
                        NewsList list = new NewsList(
                                o.getString("id"),
                                o.getString("title"),
                                o.getString("short_description"),
                                o.getString("image"),
                                o.getString("date"));
                        newsLists.add(list);
                    }
                    newsAdapter.notifyDataSetChanged();

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


    private void setupBottomNavigationView() {
        Log.d(TAG, "setupBottomNavigationView: setting up BottomNavigationView");
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNav);
        BottomNavigationHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationHelper.enableNavigation(mContext, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);

    }

}
