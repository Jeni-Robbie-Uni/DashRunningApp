package com.example.dashrunningapp.volley;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.example.dashrunningapp.SQLiteDb.DbHelper;
import com.example.dashrunningapp.activity.MainActivity;
import com.example.dashrunningapp.activity.login;
import com.example.dashrunningapp.models.AuthenticationDetails;
import com.example.dashrunningapp.models.UserDetails;

public class VolleyCustomResponses {

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

    public static void  errorResponse(VolleyError error, Context context, EditText email) {


        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Log.e("Error",error.toString());
            Toast.makeText(context, "Connection Error: Please try to register again", Toast.LENGTH_LONG).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(context, "Unauthorised Access Error", Toast.LENGTH_LONG).show();
            Log.e("Error",error.toString());
        } else if (error instanceof ServerError) {
            //TODO
            if (error.networkResponse.statusCode == 409) {
                email.setText("");
                email.setError("Account already in Use");

            } else {
                Toast.makeText(context, "Server Error: Please try and Register account again", Toast.LENGTH_LONG).show();
            }

        } else if (error instanceof NetworkError) {
            Toast.makeText(context, "Network Error: Please try and Register account again", Toast.LENGTH_LONG).show();
            Log.e("ServerError",error.toString());
        } else if (error instanceof ParseError) {
            Log.e("Error",error.toString());
            Toast.makeText(context, "Error: Please try and Register account again", Toast.LENGTH_LONG).show();

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
    public static  Response.Listener<String> GenerateTokenResponse(final Context m_context, final UserDetails current_user){
        return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                DbHelper databaseHelper= new DbHelper(m_context);
                databaseHelper.AddUser(current_user.getEmail(), current_user.getPassword());

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
    public static Response.ErrorListener GenerateSignUpError(final Context m_context, final EditText email) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyCustomResponses.errorResponse(error, m_context, email);

            }
        };
    }


}
