package com.example.dashrunningapp;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class SignUp extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Button button1 = (Button) findViewById(R.id.back_nav);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), login.class);
                view.getContext().startActivity(intent);}



        });



        Button button2 = (Button) findViewById(R.id.signup_submit_btn);



        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UserDetails current_user = new UserDetails();

                EditText f_name = (EditText)findViewById(R.id.editFirstName);
                current_user.setFirstName(f_name.getText().toString());

                EditText s_name = (EditText)findViewById(R.id.editSurname);
                current_user.setSurname(s_name.getText().toString());

                EditText email = (EditText)findViewById(R.id.editEmail);
                current_user.setEmail(email.getText().toString());

                EditText password = (EditText)findViewById(R.id.editPassword);
                current_user.setPassword(password.getText().toString());

                Toast.makeText(SignUp.this,"Hey " +
                        current_user.getFName(), Toast.LENGTH_LONG).show();


                String userJson="";
                try {


                    // convert user object to JSON
                    userJson = new Gson().toJson(current_user);

                    // print JSON string
                    System.out.println(userJson);

                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                JSONObject userJsonObject=null;

                try {
                    userJsonObject= new JSONObject(userJson);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                 Context m_context = getApplicationContext();

                // Instantiate the RequestQueue.
                RequestQueue queue = VolleyQueue.getInstance(m_context).
                        getRequestQueue();
                String url ="http://localhost:5000" + "/api/Users";

                // Request a string response from the provided URL.
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                        (Request.Method.POST, url, userJsonObject, new Response.Listener<JSONObject>() {

                            @Override
                            public void onResponse(JSONObject response) {
                                Toast.makeText(SignUp.this,"Hey " +
                                        response.toString(), Toast.LENGTH_LONG).show();

                            }
                        }, new Response.ErrorListener() {

                            @Override
                            public void onErrorResponse(VolleyError error) {
                                // TODO: Handle error
                                Toast.makeText(SignUp.this, error.toString(), Toast.LENGTH_LONG).show();
                            }
                        });



                // Add the request to the RequestQueue.
                queue.add(jsonObjectRequest);
            }




        });


    }



    //http://localhost:5000




}
