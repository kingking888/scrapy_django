package com.softaholik.bdnewstoday.utils;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * The type Volley singleton.
 */
public class VolleySingleton {
    private static VolleySingleton mInstance;
    private static Context mCtx;
    private RequestQueue mRequestQueue;

    private VolleySingleton(Context context) {
        mCtx = context;
        mRequestQueue = getRequestQueue();

        //mRequestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    /**
     * Gets instance.
     *
     * @param context the context
     * @return the instance
     */
    public static synchronized VolleySingleton getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new VolleySingleton(context);
        }

        return mInstance;
    }

    /**
     * Cancel all.
     *
     * @param requestQueue the request queue
     */
    public static void cancelAll(RequestQueue requestQueue) {

        requestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }

    /**
     * Gets request queue.
     *
     * @return the request queue
     */
    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

//    public  static void setRetryPolicy(StringRequest request){
//
//        request.setRetryPolicy(new DefaultRetryPolicy(1000, 0, 0));
//    }

    /**
     * Add to request queue.
     *
     * @param <T>     the type parameter
     * @param request the request
     */
    public <T> void addToRequestQueue(Request<T> request) {

        request.setRetryPolicy(new DefaultRetryPolicy(4000, 0, 0));
        getRequestQueue().add(request);
    }
}
