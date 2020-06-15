package com.example.dashrunningapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;


public class SignUp extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);      //display signup layout

        //Declare button variable for submit button in layout
        Button backButton = (Button) findViewById(R.id.back_nav);

        //When back button clicked navigate to parent activity i.e. login
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), login.class);
                view.getContext().startActivity(intent);}



        });


        //Declare button variable for submit button in layout
        Button button2 = (Button) findViewById(R.id.signup_submit_btn);

        //When Clicked, submit button will validate and then post input
        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Retrieve user input from specific textviews
                EditText f_name = (EditText)findViewById(R.id.editFirstName);
                EditText s_name = (EditText)findViewById(R.id.editSurname);
                EditText email = (EditText)findViewById(R.id.editEmail);
                EditText password = (EditText)findViewById(R.id.editPassword);

                //Validate User Details


                //Create instance of user details
                UserDetails current_user= new UserDetails(f_name.getText().toString(),s_name.getText().toString(),email.getText().toString(),password.getText().toString());

                //Create a JSON object for post request
                JSONObject userJsonObject = null;
                try {
                    userJsonObject = JsonFormater.convertToJsonObj(JsonFormater.convertToJString(current_user));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //define context for post method
                 Context m_context = getApplicationContext();

                // Instantiate the RequestQueue.
                RequestQueue queue = VolleyQueue.getInstance(m_context).
                        getRequestQueue();
                String url ="http://localhost:5000" + "/api/Users";

                // Request a string response from the provided URL.
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url, userJsonObject, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {       //If post successful it does this
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
