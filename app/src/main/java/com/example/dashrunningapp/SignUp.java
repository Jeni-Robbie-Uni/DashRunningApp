package com.example.dashrunningapp;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

public class SignUp extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);      //display signup layout

        //Declare button variable for submit button in layout
        final Button submitButton = (Button) findViewById(R.id.signup_submit_btn);
        //Declare button variable for submit button in layout
        final Button backButton = (Button) findViewById(R.id.back_nav);

        //Retrieve user input from specific textviews
        final EditText f_name = (EditText)findViewById(R.id.editFirstName);
        final EditText s_name = (EditText)findViewById(R.id.editSurname);
        final EditText email = (EditText)findViewById(R.id.editEmail);
        final EditText password = (EditText)findViewById(R.id.editPassword);


        //When back button clicked navigate to parent activity i.e. login
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), login.class);
                view.getContext().startActivity(intent);}



        });


        //When Clicked, submit button will validate and then post input
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                //Validate User Details


                //Email validation


                //Create instance of user details
                UserDetails current_user= new UserDetails(f_name.getText().toString(),s_name.getText().toString(),email.getText().toString(),password.getText().toString());

                //Create a JSON object for post request
                JSONObject userJsonObject = null;
                try {
                    userJsonObject = JsonFormater.convertToJsonObj(JsonFormater.convertToJString(current_user));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //final String jsonData = userJsonObject == null ? null : userJsonObject.toString();

                //define context for post method
                 Context m_context = getApplicationContext();

                // Instantiate the RequestQueue.
                RequestQueue queue = VolleyQueue.getInstance(m_context).
                        getRequestQueue();
                String url ="http://localhost:5000" + "/api/Users";

                /*
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i("VOLLEY", response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("VOLLEY", error.toString());
                    }
                }) {
                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8";
                    }
                    //cant convert bytes= volley cant send to server i.e cause its custom request or corript
                    @Override
                    public byte[] getBody() {
                        try {
                            return jsonData == null ? null : jsonData.getBytes("utf-8");
                        } catch (UnsupportedEncodingException uee) {
                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonData, "utf-8");
                            return null;
                        }
                    }
                    @Override
                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                        String responseString = "";
                        if (response != null) {
                            responseString = String.valueOf(response.statusCode);
                            // can get more details such as response.headers
                        }
                        return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                    }
                }; */


                //Request a string response from the provided URL.

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url, userJsonObject, new Response.Listener() {

                            @Override
                            public void onResponse(Object response) {
                                Toast.makeText(SignUp.this,"Hey " +
                                        response.toString(), Toast.LENGTH_LONG).show();



                            }
                        }, new Response.ErrorListener() {       //If post unsuccessful it does this
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                                Toast.makeText(SignUp.this, error.toString(), Toast.LENGTH_LONG).show();    //Add error message to screen via toast
                            }
                        });


                // Add the request to the RequestQueue.
                queue.add(jsonObjectRequest);







            }

        });


    }

}
