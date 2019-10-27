package com.softaholik.bdnewstoday.activities.main;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.softaholik.bdnewstoday.R;
import com.softaholik.bdnewstoday.adapters.NewsAdapter;
import com.softaholik.bdnewstoday.models.NewsList;
import com.softaholik.bdnewstoday.utils.BottomNavigationHelper;
import com.softaholik.bdnewstoday.utils.SessionManager;
import com.softaholik.bdnewstoday.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsActivity extends AppCompatActivity {
    private static final String TAG = "NewsActivity";
    private static final int ACTIVITY_NUM = 1;
    private Context mContext = NewsActivity.this;


    private static final String BASE_URL = "https://newsapi.pythonanywhere.com/api/v1/";
    private static final String URL_NEWS ="news/";
    private static final String URL_NEWS_CATEGORY ="news/category/";
    private static final String URL_NEWS_SEARCH="/?search=";
    private static final String ACCESS_TOKEN = "Token 1c69b41523332fd3e72c9194063db9238eaf9741";

    private RadioButton rb_category_bd,rbCategory;
    private NewsAdapter newsAdapter;
    private RadioGroup rgCategory;
    private int selectedId ;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private TextView emptyView;
    private ProgressBar progressBar ;
    private LinearLayoutManager manager;
    private List<NewsList> newsLists;
    private RequestQueue requestQueue;
    private String next;
    private SkeletonScreen skeletonScreen;
    private SessionManager sessionManager;
    //private RecyclerView.Adapter adapter;
    //use this instead

    //for scroll view
    private boolean isScrolling = false , isNewsData = false ;
    private int currentItems ,totalItems , scrollOutItems ,page_count=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        sessionManager = new SessionManager(mContext);

        if(sessionManager.loadNightModeState()){
            setTheme(R.style.DarkTheme);
        }else {
            setTheme(R.style.AppTheme);
        }


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        setupBottomNavigationView();
        initSetup();

        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swipeRefreshLayout);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               // refreshData(); // your code
                progressBar.setVisibility(View.GONE);
                selectedId = rgCategory.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                rbCategory = findViewById(selectedId);
                Log.d(TAG, "onRefresh: "+rbCategory.getTag().toString());
                loadNewsData(rbCategory.getTag().toString());
                pullToRefresh.setRefreshing(false);
            }
        });

    }



    private void initSetup(){
        recyclerView = findViewById(R.id.news_recyclerView);
        emptyView = findViewById(R.id.empty_view);
        progressBar = findViewById(R.id.progressBar);
        rgCategory = findViewById(R.id.main_rg_category);
        searchView = findViewById(R.id.searchView);

        //radio button for categories
        rb_category_bd = findViewById(R.id.rb_category_bd);


        rb_category_bd.setChecked(true);


        recyclerView.setHasFixedSize(true);
        manager =new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(manager);
        newsLists = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsLists,mContext);

        recyclerView.setAdapter(newsAdapter);

        loadNewsData(rb_category_bd.getTag().toString());

        rgCategory.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                newsLists.clear();

                selectedId = rgCategory.getCheckedRadioButtonId();

                // find the radiobutton by returned id
                rbCategory = findViewById(selectedId);
                searchView.setQueryHint("Search " + rbCategory.getTag().toString() + " news");

                VolleySingleton.getInstance(mContext).getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
                    @Override
                    public boolean apply(Request<?> request) {
                        return true;
                    }
                });

                loadNewsData(rbCategory.getTag().toString());
            }
        });


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


                filter(s);

                return false;
            }
            private void filter(String s) {



                    selectedId = rgCategory.getCheckedRadioButtonId();

                    // find the radiobutton by returned id
                    rbCategory = findViewById(selectedId);
                    Log.d(TAG, "filter: "+rbCategory.getTag());
                    loadSearchNewsData(rbCategory.getTag().toString(),s.trim());



            }
        });


        //recyclerView.smoothScrollToPosition(0);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                                Toast.makeText(mContext,"No more results", Toast.LENGTH_SHORT).show();
                            }

                            //loadMedData(page_count);
                        }
                    });


                }
            }
        });






    }


    public void loadNewsData(final String category){

        skeletonScreen = Skeleton.bind(recyclerView)
                .adapter(newsAdapter)
                .load(R.layout.item_skeleton)
                .show();

        isNewsData = true;

        if (!newsLists.isEmpty()){
            newsLists.clear();
        }
        newsAdapter.notifyDataSetChanged();

        //String REQUEST_URL=BASE_URL+URL_MEDICINE_DATA_PAGINATION+page_count;
        String REQUEST_URL=BASE_URL+ URL_NEWS_CATEGORY +category;

        progressBar.setVisibility(View.GONE);
        Log.d(TAG, "loadNewsData: "+category+"  "+REQUEST_URL);
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading Data...");
//        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, REQUEST_URL,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject  s) {
                progressBar.setVisibility(View.INVISIBLE);
                skeletonScreen.hide();
//                progressDialog.dismiss();
                try {
                    next = s.getString("next");

                    JSONArray jsonArray = s.getJSONArray("results");
                    if (!newsLists.isEmpty()){
                        newsLists.clear();
                    }

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

    }


    public void loadSearchNewsData(final String category,final String search){
        skeletonScreen = Skeleton.bind(recyclerView)
                .adapter(newsAdapter)
                .load(R.layout.view_news_item)
                .show();

        isNewsData = true;

        if (!newsLists.isEmpty()){
            newsLists.clear();
        }
        newsAdapter.notifyDataSetChanged();

        //String REQUEST_URL=BASE_URL+URL_MEDICINE_DATA_PAGINATION+page_count;

        String REQUEST_URL=BASE_URL+ URL_NEWS_CATEGORY+category+URL_NEWS_SEARCH+search;

        progressBar.setVisibility(View.VISIBLE);
        Log.d(TAG, "loadSearchNewsData: "+REQUEST_URL);
//        final ProgressDialog progressDialog = new ProgressDialog(this);
//        progressDialog.setMessage("Loading Data...");
//        progressDialog.show();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, REQUEST_URL,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject  s) {
                progressBar.setVisibility(View.INVISIBLE);
                skeletonScreen.hide();
//                progressDialog.dismiss();
                try {

                    JSONArray jsonArray = s.getJSONArray("results");
                    if (jsonArray.length() < 1) {
                        Log.d(TAG, "onResponse: no order");
                        recyclerView.setVisibility(View.INVISIBLE);
                        emptyView.setVisibility(View.VISIBLE);

                    } else {
                        recyclerView.setVisibility(View.VISIBLE);
                        emptyView.setVisibility(View.GONE);

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

                    }


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

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        return gestureDetector.onTouchEvent(event);
//    }

    @Override
    protected void onStop() {
        super.onStop();

        VolleySingleton.getInstance(mContext).getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        VolleySingleton.getInstance(mContext).getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }



}
