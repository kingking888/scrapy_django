package com.softaholik.bdnewstoday.activities.main;

import android.content.Context;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

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
    private RecyclerView recyclerViewSearch,recyclerViewBd,recyclerViewPolitics,recyclerViewEconomy,
            recyclerViewSports,recyclerViewInternational,recyclerViewTechnology,recyclerViewEntertainment;

    private static final String BASE_URL = "https://newsapi.pythonanywhere.com/api/v1/";
    private static final String URL_NEWS ="news/";
    private static final String URL_NEWS_RECENT ="news/recent/";
    private static final String URL_NEWS_SEARCH="?search=";
    private static final String ACCESS_TOKEN = "Token 1c69b41523332fd3e72c9194063db9238eaf9741";

    private NestedScrollView highlightNsv;
    private NewsAdapter newsAdapter;
    private SearchView searchView;
    private TextView emptyView;
    private LinearLayoutManager searchLayoutManager,bdLayoutManager,politicsLayoutManager,
            economyLayoutManager,sportsLayoutManager,internationalLayoutManager,
            technologyLayoutManager,entertainmentLayoutManager;
    private List<NewsList> searchNewsLists, bdNewsLists,politicsNewsLists,economyNewsLists,
            sportsNewsLists,internationalNewsLists,technologyNewsLists,entertainmentNewsLists;
    private String[] newsCategory = {"bangladesh", "politics", "economy", "sports", "international", "technology", "entertainment"};
    private RequestQueue requestQueue;
    private String next;
    private SkeletonScreen skeletonScreenBd,skeletonScreenPolitics,skeletonScreenEconomy,
            skeletonScreenSports,skeletonScreenInternational,skeletonScreenTechnology,skeletonScreenEntertainment;
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
        setContentView(R.layout.activity_highlight);

        setupBottomNavigationView();
        initSetup();


    }
    private void initSetup(){
        highlightNsv = findViewById(R.id.highlight_nsv);
        recyclerViewSearch = findViewById(R.id.recycler_view_search);
        recyclerViewBd = findViewById(R.id.recyclerViewBd);
        recyclerViewPolitics = findViewById(R.id.recyclerViewPolitics);
        recyclerViewEconomy = findViewById(R.id.recyclerViewEconomy);
        recyclerViewSports = findViewById(R.id.recyclerViewSports);
        recyclerViewInternational = findViewById(R.id.recyclerViewInternational);
        recyclerViewTechnology = findViewById(R.id.recyclerViewTechnology);
        recyclerViewEntertainment = findViewById(R.id.recyclerViewEntertainment);



        emptyView = findViewById(R.id.empty_view);
        progressBar = findViewById(R.id.progressBar);
        searchView = findViewById(R.id.searchViewHighlight);

        recyclerViewSearch.setHasFixedSize(true);
        searchLayoutManager =new LinearLayoutManager(mContext);
        bdLayoutManager = new LinearLayoutManager(mContext);
        politicsLayoutManager = new LinearLayoutManager(mContext);
        economyLayoutManager = new LinearLayoutManager(mContext);
        sportsLayoutManager = new LinearLayoutManager(mContext);
        internationalLayoutManager = new LinearLayoutManager(mContext);
        technologyLayoutManager = new LinearLayoutManager(mContext);
        entertainmentLayoutManager = new LinearLayoutManager(mContext);

        recyclerViewSearch.setLayoutManager(searchLayoutManager);
        recyclerViewBd.setLayoutManager(bdLayoutManager);
        recyclerViewPolitics.setLayoutManager(politicsLayoutManager);
        recyclerViewEconomy.setLayoutManager(economyLayoutManager);
        recyclerViewSports.setLayoutManager(sportsLayoutManager);
        recyclerViewInternational.setLayoutManager(internationalLayoutManager);
        recyclerViewTechnology.setLayoutManager(technologyLayoutManager);
        recyclerViewEntertainment.setLayoutManager(entertainmentLayoutManager);

        searchNewsLists = new ArrayList<>();

        bdNewsLists = new ArrayList<>();
        politicsNewsLists = new ArrayList<>();
        economyNewsLists = new ArrayList<>();
        sportsNewsLists = new ArrayList<>();
        internationalNewsLists = new ArrayList<>();
        technologyNewsLists = new ArrayList<>();
        entertainmentNewsLists = new ArrayList<>();


        if (!bdNewsLists.isEmpty()){
            bdNewsLists.clear();
        }
        for (int i = 0; i < newsCategory.length; i++) {
            Log.d(TAG, "initSetup: "+newsCategory[i]);
            loadNewsData(newsCategory[i]);
        }
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
                    highlightNsv.setVisibility(View.VISIBLE);
                }else{
                    highlightNsv.setVisibility(View.GONE);
                    recyclerViewSearch.setVisibility(View.VISIBLE);
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




        final SwipeRefreshLayout pullToRefresh = findViewById(R.id.swipeRefreshLayout);

        pullToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {


                if (!bdNewsLists.isEmpty()){
                    bdNewsLists.clear();
                }
                if (!politicsNewsLists.isEmpty()){
                    politicsNewsLists.clear();
                }
                if (!economyNewsLists.isEmpty()){
                    economyNewsLists.clear();
                }
                if (!sportsNewsLists.isEmpty()){
                    sportsNewsLists.clear();
                }
                if (!internationalNewsLists.isEmpty()){
                    internationalNewsLists.clear();
                }
                if (!technologyNewsLists.isEmpty()){
                    technologyNewsLists.clear();
                }
                if (!entertainmentNewsLists.isEmpty()){
                    entertainmentNewsLists.clear();
                }
                if (!searchNewsLists.isEmpty()){
                    searchNewsLists.clear();
                }
                for (int i = 0; i < newsCategory.length; i++) {
                    Log.d(TAG, "initSetup: "+newsCategory[i]);
                    loadNewsData(newsCategory[i]);
                }
                pullToRefresh.setRefreshing(false);
            }
        });

    }

    public void loadNewsData(final String category){

        //String REQUEST_URL=BASE_URL+URL_MEDICINE_DATA_PAGINATION+page_count;

        String REQUEST_URL=null;


        REQUEST_URL=BASE_URL +URL_NEWS_RECENT+category;

        Log.d(TAG, "loadNewsData: "+REQUEST_URL);

        skeletonScreenBd = Skeleton.bind(recyclerViewBd)
                .adapter(newsAdapter)
                .count(3)
                .load(R.layout.item_skeleton)
                .show();
        skeletonScreenPolitics = Skeleton.bind(recyclerViewPolitics)
                .adapter(newsAdapter)
                .count(3)
                .load(R.layout.item_skeleton)
                .show();

        skeletonScreenEconomy = Skeleton.bind(recyclerViewEconomy)
                .adapter(newsAdapter)
                .count(3)
                .load(R.layout.item_skeleton)
                .show();
        skeletonScreenSports = Skeleton.bind(recyclerViewSports)
                .adapter(newsAdapter)
                .count(3)
                .load(R.layout.item_skeleton)
                .show();
        skeletonScreenInternational = Skeleton.bind(recyclerViewInternational)
                .adapter(newsAdapter)
                .count(3)
                .load(R.layout.item_skeleton)
                .show();

        skeletonScreenTechnology = Skeleton.bind(recyclerViewTechnology)
                .adapter(newsAdapter)
                .count(3)
                .load(R.layout.item_skeleton)
                .show();
        skeletonScreenEntertainment = Skeleton.bind(recyclerViewEntertainment)
                .adapter(newsAdapter)
                .count(3)
                .load(R.layout.item_skeleton)
                .show();




//        skeletonScreen = Skeleton.bind(recyclerViewBd)
//                .adapter(newsAdapter)
//                .load(R.layout.view_news_item)
//                .count(3)
//                .shimmer(true)
//                .angle(20)
//                .frozen(false)
//                .show();
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

                    JSONArray jsonArray = s.getJSONArray("results");


                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject o=jsonArray.getJSONObject(i);
                        NewsList list = new NewsList(
                                o.getString("id"),
                                o.getString("title"),
                                o.getString("short_description"),
                                o.getString("image"),
                                o.getString("date"));
                        if(category.equals("bangladesh")){
                            bdNewsLists.add(list);
                        }
                        if(category.equals("politics")){
                            politicsNewsLists.add(list);
                        }
                        if(category.equals("economy")){
                            economyNewsLists.add(list);
                        }
                        if(category.equals("sports")){
                            sportsNewsLists.add(list);
                        }
                        if(category.equals("international")){
                            internationalNewsLists.add(list);
                        }
                        if(category.equals("technology")){
                            technologyNewsLists.add(list);
                        }
                        if(category.equals("entertainment")){
                            entertainmentNewsLists.add(list);
                        }

                    }




                    if(category.equals("bangladesh")){
                        skeletonScreenBd.hide();
                        newsAdapter = new NewsAdapter(bdNewsLists,mContext);
                        recyclerViewBd.setAdapter(newsAdapter);
                    }
                    if(category.equals("politics")){
                        skeletonScreenPolitics.hide();
                        newsAdapter = new NewsAdapter(politicsNewsLists,mContext);
                        recyclerViewPolitics.setAdapter(newsAdapter);
                    }
                    if(category.equals("economy")){
                        skeletonScreenEconomy.hide();
                        newsAdapter = new NewsAdapter(economyNewsLists,mContext);
                        recyclerViewEconomy.setAdapter(newsAdapter);
                    }
                    if(category.equals("sports")){
                        skeletonScreenSports.hide();
                        newsAdapter = new NewsAdapter(sportsNewsLists,mContext);
                        recyclerViewSports.setAdapter(newsAdapter);
                    }
                    if(category.equals("international")){
                        skeletonScreenInternational.hide();
                        newsAdapter = new NewsAdapter(internationalNewsLists,mContext);
                        recyclerViewInternational.setAdapter(newsAdapter);
                    }
                    if(category.equals("technology")){
                        skeletonScreenTechnology.hide();
                        newsAdapter = new NewsAdapter(technologyNewsLists,mContext);
                        recyclerViewTechnology.setAdapter(newsAdapter);
                    }
                    if(category.equals("entertainment")){
                        skeletonScreenEntertainment.hide();
                        newsAdapter = new NewsAdapter(entertainmentNewsLists,mContext);
                        recyclerViewEntertainment.setAdapter(newsAdapter);
                    }
//                    newsAdapter.notifyDataSetChanged();

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


    public void loadSearchNewsData(final String search){

        isNewsData = true;

        if (!searchNewsLists.isEmpty()){
            searchNewsLists.clear();
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
                        searchNewsLists.add(list);
                    }
                    newsAdapter = new NewsAdapter(searchNewsLists,mContext);
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
                        searchNewsLists.add(list);
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
