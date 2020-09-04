package com.example.dashrunningapp.volley;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
//wrapping around volley request queue into Singleton class that creates or retrieves the queue
//followed android studio tutorial found in report appendices
//Only one instance of class is made at splashscreen and persisted for the lifetime of the application
public class VolleyQueue {

    private static VolleyQueue instance;
    private RequestQueue requestQueue;
    private static Context m_context;

        //private constructor
    private VolleyQueue(Context context) {
        m_context = context;
        requestQueue = getRequestQueue();   //

    }
    //public getter. if instance of volley queue object exists it will return it, if not will create one
    public static synchronized VolleyQueue getInstance(Context context) {
        if (instance == null) {
            instance = new VolleyQueue(context);    //if
        }
        return instance;
    }
//public getter. if a request queue exists (created when volloy queue object instansiated) it will return it, if not will create one
    public RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(m_context.getApplicationContext());
        }
        return requestQueue;
    }
//Adds all requests to queue of any type
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


}