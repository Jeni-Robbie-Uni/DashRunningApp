package com.example.dashrunningapp.activity;

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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.dashrunningapp.SQLiteDb.DbHelper;
import com.example.dashrunningapp.Misc.JsonFormater;
import com.example.dashrunningapp.Misc.ApiStringConstants;
import com.example.dashrunningapp.models.UserDetails;
import com.example.dashrunningapp.volley.VolleyCustomResponses;
import com.example.dashrunningapp.exceptions.NoStoredUserException;
import com.example.dashrunningapp.exceptions.TooManyUsersException;
import com.example.dashrunningapp.volley.VolleyQueue;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

//Splash Screen allows for app Loading wait screen
//In splash screen application checks if user details are stored in SQL lite database and sends to api to get auth token
//If it comes across any error server/ authorization it opens login screen else will open main activity for custom experience
//If db doesnt exits It creates one i.e. first launch
public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //Get app context
        final Context m_context = getApplicationContext();


        //Create instance of database helper to interact with device internal database
        DbHelper databaseHelper = new DbHelper(SplashScreen.this);


        //If database exists, check if there are stored credentials and store
        UserDetails tempUserCheck;
        try {
            tempUserCheck = databaseHelper.checkUserExist();
        } catch (NoStoredUserException e) {
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
            e.printStackTrace();
            return;
        } catch (TooManyUsersException e) {
            e.printStackTrace();
            databaseHelper.DropUserTable();
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
            return;
        }


        //Create a JSON object for post request to format string for API
        JSONObject userJsonObject;
        try {
            //Convert UserDetails to Json Object before converting to string for request
            userJsonObject = JsonFormater.convertToJsonObj(JsonFormater.convertToJString(tempUserCheck));
        } catch (Exception ex) {    //If fails just have user login manually
            ex.printStackTrace();
            Intent intentLogin = new Intent(this, login.class);
            startActivity(intentLogin);
            return;
        }

        //This should never be null as database helper throws that exception
        final String jsonData = userJsonObject.toString();


        // Instantiate the RequestQueue or get current queue
        final RequestQueue requestQueue = VolleyQueue.getInstance(m_context).getRequestQueue();

        //Create server request address
        String url = ApiStringConstants.getServerAddress() + ApiStringConstants.getLogin();

        //send post request with custom response and error handler
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, VolleyCustomResponses.GenerateTokenResponse(m_context), VolleyCustomResponses.GenerateTokenError(m_context)) {
            @Override
            public String getBodyContentType() {
                return ApiStringConstants.getCharset();    //allows volley to read API response
            }

            //converted string to be sent into bytes. if not volley cant send to server
            @Override
            public byte[] getBody() {
                try {
                    return jsonData.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonData, "utf-8");
                    return null;
                }
            }
            //define how response is read and determine if request was successful or not
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
            requestQueue.add(stringRequest);
        } catch (Exception ex) {            //If token can not be generated they must login
            Log.e("Error", ex.toString());
            Intent intent = new Intent(this, login.class);
            startActivity(intent);
        }


    }

}


