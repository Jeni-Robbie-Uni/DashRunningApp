package com.example.dashrunningapp;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

class VolleyCustomResponses {

    public static void  errorResponse(VolleyError error, Context context) {

        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
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

    public static  Response.Listener<String> GenerateTokenResponse(final Context m_context){
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AuthenticationDetails x = AuthenticationDetails.getInstance();
                AuthenticationDetails.setToken(response);
                Intent intent = new Intent(m_context, MainActivity.class);
                m_context.startActivity(intent);
            }
        };
    }
    public static Response.ErrorListener GenerateTokenError(final Context m_context) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyCustomResponses.errorResponse(error, m_context);
                Intent intent = new Intent(m_context, login.class);
                m_context.startActivity(intent);
            }
        };
    }


}
