package com.example.dashrunningapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public class VolleyCustomError {


    public static void  errorResponse(VolleyError error, Context context) {

        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Toast.makeText(context,
                    "no connection",
                    Toast.LENGTH_LONG).show();
            Log.e("Error",error.toString());
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(context,
                    "no author",
                    Toast.LENGTH_LONG).show();
            Log.e("Error",error.toString());
        } else if (error instanceof ServerError) {
            Toast.makeText(context,
                    "servererror",
                    Toast.LENGTH_LONG).show();
            Log.e("ServerError",error.toString());

        } else if (error instanceof NetworkError) {
            Toast.makeText(context,
                    "networkerror",
                    Toast.LENGTH_LONG).show();
            Log.e("ServerError",error.toString());
        } else if (error instanceof ParseError) {
            Log.e("ServerError",error.toString());
        }
    }


}
