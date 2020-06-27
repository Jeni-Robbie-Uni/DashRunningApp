package com.example.dashrunningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.dashrunningapp.exceptions.NoStoredUserException;
import com.example.dashrunningapp.exceptions.TooManyUsersException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.io.UnsupportedEncodingException;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        DbHelper databaseHelper = new DbHelper(SplashScreen.this);
        try {
            UserDetails tempUserCheck = databaseHelper.checkUserExist();
            final Context m_context = getApplicationContext();
            // Instantiate the RequestQueue.
            final RequestQueue queue = VolleyQueue.getInstance(m_context).
                    getRequestQueue();

            //Authorise with API
            String url = "http://localhost:5000" + "/api/token";
            StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    Log.i("VOLLEY", response);
                    AuthenticationDetails x= AuthenticationDetails.getInstance();
                    AuthenticationDetails.setToken(response);


                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Toast.makeText(m_context,
                                "no connection",
                                Toast.LENGTH_LONG).show();
                    } else if (error instanceof AuthFailureError) {
                        Toast.makeText(m_context,
                                "no author",
                                Toast.LENGTH_LONG).show();
                        //TODO
                    } else if (error instanceof ServerError) {
                        //TODO

                            Toast.makeText(m_context,
                                    "servererror",
                                    Toast.LENGTH_LONG).show();

                    } else if (error instanceof NetworkError) {
                        //TODO
                        Toast.makeText(m_context,
                                "networkerror",
                                Toast.LENGTH_LONG).show();
                    } else if (error instanceof ParseError) {
                        //TODO
                    }
                }


            });

            try {
                queue.add(stringRequest);
            } catch (Exception ex) {
                Log.e("Error", ex.toString());
            }


            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } catch (NoStoredUserException e) {
            e.printStackTrace();
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        } catch (TooManyUsersException e) {
            e.printStackTrace();
            databaseHelper.DropUserTable();
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        }





    }


}


