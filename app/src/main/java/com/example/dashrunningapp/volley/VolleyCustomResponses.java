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
import com.example.dashrunningapp.Misc.ErrorStrings;
import com.example.dashrunningapp.SQLiteDb.DbHelper;
import com.example.dashrunningapp.activity.MainActivity;
import com.example.dashrunningapp.activity.login;
import com.example.dashrunningapp.models.AuthenticationDetails;
import com.example.dashrunningapp.models.UserDetails;

public class VolleyCustomResponses {

    //general error response with custom toast messages
    public static void  errorResponse(VolleyError error, Context context) {

        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Toast.makeText(context, ErrorStrings.getTimeoutError(), Toast.LENGTH_LONG).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(context, ErrorStrings.getLoginFailureError(), Toast.LENGTH_LONG).show();
        } else if (error instanceof ServerError) {
            Toast.makeText(context, ErrorStrings.getServerError(), Toast.LENGTH_LONG).show();
        } else if (error instanceof NetworkError) {
            Toast.makeText(context, ErrorStrings.getNetworkError(), Toast.LENGTH_LONG).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(context, ErrorStrings.getUnrecoverableError(), Toast.LENGTH_LONG).show();
        }
        Log.e("Error",error.toString());
    }

    //Overloaded above function to deal with email account error only seen on sign up
    public static void  errorResponse(VolleyError error, Context context, EditText email) {


        if (error instanceof TimeoutError || error instanceof NoConnectionError) {
            Toast.makeText(context, ErrorStrings.getTimeoutError(), Toast.LENGTH_LONG).show();
        } else if (error instanceof AuthFailureError) {
            Toast.makeText(context, ErrorStrings.getLoginFailureError(), Toast.LENGTH_LONG).show();
        } else if (error instanceof ServerError) {
            if (error.networkResponse.statusCode == 409) {
                email.setText("");
                email.setError(ErrorStrings.getAccountTakenError());

            } else {
                Toast.makeText(context, ErrorStrings.getServerError(), Toast.LENGTH_LONG).show();
            }
        } else if (error instanceof NetworkError) {
            Toast.makeText(context, ErrorStrings.getNetworkError(), Toast.LENGTH_LONG).show();
        } else if (error instanceof ParseError) {
            Toast.makeText(context, ErrorStrings.getUnrecoverableError(), Toast.LENGTH_LONG).show();
        }
        Log.e("Error",error.toString());
    }
    //On successful volley response stores token in authentication object
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
    //On successful volley response stores token in authentication object and credential in the database helper if registering
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

    //Error listeners to call specific error handlers depending on the information above//
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
    //Error listeners to call specific error handlers depending on the information above//
    public static Response.ErrorListener GenerateSignUpError(final Context m_context, final EditText email) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyCustomResponses.errorResponse(error, m_context, email);

            }
        };
    }


}
