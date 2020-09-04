package com.example.dashrunningapp.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.dashrunningapp.Misc.ErrorStrings;
import com.example.dashrunningapp.Misc.JsonFormater;
import com.example.dashrunningapp.R;
import com.example.dashrunningapp.Misc.ApiStringConstants;
import com.example.dashrunningapp.Misc.UserDetailValidation;
import com.example.dashrunningapp.models.UserDetails;
import com.example.dashrunningapp.volley.VolleyCustomResponses;
import com.example.dashrunningapp.volley.VolleyQueue;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

//Login activity class is responsible for managing user login or from the UI navigate to sign up
public class login extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {    //oncreation of login activity do x
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);     //show layout

        //assign button objects to corresponding UI references
        Button signUpBtn = (Button) findViewById(R.id.SignUpLink);
        Button loginBtn = (Button) findViewById(R.id.loginBtn);

        //define context for post method
        final Context m_context = getApplicationContext();




        //set button on click listener for sign up button to redirect to sign up activity
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SignUp.class);    //set intent i.e. facilitate activity communication
                view.getContext().startActivity(intent);    //redirect to sign up class
            }
        });
        //set button on click listener for sign up button to redirect to sign up activity
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //assign edit text here so that eview can be changed withing function
                EditText email = findViewById(R.id.editEmail);
                EditText password = findViewById(R.id.editPassword);

                //Only true if all fields i.e. email and password entry is valid
                boolean allValid= true;

                // Validate email
                if (!UserDetailValidation.IsEmailValid(email)) {
                    email.setError(ErrorStrings.getInvalidEmailError());
                    email.setText("");  //reset field
                    allValid=false;
                }

                //validate password
                if (UserDetailValidation.IsFieldEmpty(password.getText().toString()))
                {
                    password.setError(ErrorStrings.getEmptyFieldError());
                    password.setText("");   //reset field
                    allValid=false;
                }

                if (allValid){

                    //Create instance of user data with validated parameters
                    UserDetails currentUser = new UserDetails(email.getText().toString(), password.getText().toString());

                    //Create a JSON object for post request
                    JSONObject userJsonObject;
                    try {
                        userJsonObject = JsonFormater.convertToJsonObj(JsonFormater.convertToJString(currentUser));
                    } catch (Exception ex) {
                        Log.i("Error", ex.toString());
                        Toast.makeText(m_context, ErrorStrings.getLoctaionNotFoundError(), Toast.LENGTH_LONG).show();
                        return; //exit function as API request will be unable to be made
                    }

                    //convert jason object to string for string request
                    final String userJsonData = userJsonObject.toString();

                    // Instantiate the RequestQueue or get existing queue
                    final RequestQueue queue = VolleyQueue.getInstance(m_context).getRequestQueue();

                    //Set appropriate API url
                    String url = ApiStringConstants.getServerAddress() + ApiStringConstants.getLogin();

                    //Send Post Request. Call response  and error handler and define how response is read and or sent
                    StringRequest stringLoginRequest = new StringRequest(Request.Method.POST, url, VolleyCustomResponses.GenerateTokenResponse(m_context, currentUser), VolleyCustomResponses.GenerateTokenError(m_context))
                    {
                        @Override
                        public String getBodyContentType() {
                            return ApiStringConstants.getCharset();   //How to read response from API
                        }

                        //convert string to be sent into bytes. if not volley cant send to server
                        @Override
                        public byte[] getBody() {
                            try {
                                return userJsonData.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", userJsonData, "utf-8");
                                return null;
                            }
                        }
                        //define how response is read and determine if request was successful or not
                        @Override
                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
                            if (response != null) {
                                String responseString = new String(response.data);
                                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                            }
                            return Response.error(new VolleyError());
                        }
                    };

                    try {
                        queue.add(stringLoginRequest);  //add to request to queue
                    } catch (Exception ex) {
                        Log.e("Error", ex.toString());
                        Toast.makeText(m_context, ErrorStrings.getUnrecoverableError(), Toast.LENGTH_LONG).show();
                    }

                }

            }
        });


    }


}
