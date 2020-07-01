package com.example.dashrunningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.dashrunningapp.exceptions.NoStoredUserException;
import com.example.dashrunningapp.exceptions.TooManyUsersException;

import org.json.JSONObject;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Get app context
        final Context m_context = getApplicationContext();


        //Create instance of database helper
        DbHelper databaseHelper = new DbHelper(SplashScreen.this);


        try {
            //If database exists, check if there are stored credentials and store
            UserDetails tempUserCheck = databaseHelper.checkUserExist();

            //Create a JSON object for post request to format string for API
            JSONObject userJsonObject = null;
            try {
                //Convert UserDetails to Json Object before converting to string for request
                userJsonObject = JsonFormater.convertToJsonObj(JsonFormater.convertToJString(tempUserCheck));
            } catch (Exception ex) {    //If fails just have user login manually
                Log.i("Exception:", ex.toString());
                Intent intentLogin = new Intent(this, login.class);
                startActivity(intentLogin);
            }

            //This should never be null as database helper throws that exception
           if (userJsonObject==null){
               Intent intentLogin = new Intent(this, login.class);
               startActivity(intentLogin);
           }
            final String jsonData = userJsonObject.toString();


            // Instantiate the RequestQueue.
            final RequestQueue queue = VolleyQueue.getInstance(m_context).
                    getRequestQueue();

            //Authorise with API

            //PUTAPIString constants in class
            String url =m_context.getString(R.string.server_Address) + m_context.getString(R.string.login_api);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, url, GenerateTokenResponse(m_context), GenerateTokenError(m_context)){
                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {     //Overridden to get post request response body
                    String responseString;
                    if (response != null) {
                        responseString = new String(response.data);
                        // can get more details such as response.headers
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }
                    return Response.error(new VolleyError());
                }
            };

            try {
                queue.add(stringRequest);
            } catch (Exception ex) {            //If token can not be generated they must login
                Log.e("Error", ex.toString());
                Intent intent = new Intent(this, login.class);
                startActivity(intent);
            }

        } catch (NoStoredUserException ex) { //If no users stored  make user login
            Log.e("Catch", ex.toString());
            Intent intent = new Intent(this, login.class);
            startActivity(intent);

        } catch (TooManyUsersException ex) { //If more one user stored drop table and make user login
            Log.e("Catch", ex.toString());
            databaseHelper.DropUserTable();
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        }
    }


    ////////////////////////////////////////////////////////////////////////////////
    //Custom Volley response/ error Listeners////////////////////////////////////


    private Response.Listener<String> GenerateTokenResponse(final Context m_context){
       return new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                AuthenticationDetails x = AuthenticationDetails.getInstance();
                AuthenticationDetails.setToken(response);
                Intent intent = new Intent(m_context, MainActivity.class);
                startActivity(intent);
            }
        };
    }
    private Response.ErrorListener GenerateTokenError(final Context m_context) {
        return new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyCustomError.errorResponse(error, m_context);
                Intent intent = new Intent(m_context, login.class);
                startActivity(intent);
            }
        };
    }

}


