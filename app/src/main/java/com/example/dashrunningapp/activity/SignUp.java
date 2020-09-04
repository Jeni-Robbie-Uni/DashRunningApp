package com.example.dashrunningapp.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;


import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.dashrunningapp.Misc.ErrorStrings;
import com.example.dashrunningapp.SQLiteDb.DbHelper;
import com.example.dashrunningapp.Misc.JsonFormater;
import com.example.dashrunningapp.R;
import com.example.dashrunningapp.Misc.ApiStringConstants;
import com.example.dashrunningapp.Misc.UserDetailValidation;
import com.example.dashrunningapp.models.UserDetails;
import com.example.dashrunningapp.volley.VolleyCustomResponses;
import com.example.dashrunningapp.exceptions.NoStoredUserException;
import com.example.dashrunningapp.exceptions.TooManyUsersException;
import com.example.dashrunningapp.volley.VolleyQueue;


import org.json.JSONObject;

import java.io.UnsupportedEncodingException;



//Sign up activity class is responsible for regestration. Validates user credentials then sends request to API.
public class SignUp extends AppCompatActivity {

    //variables accessed by multiple override functions
    private PopupWindow mPopupWindow;
    private Boolean agree=false;
    private UserDetails currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);      //display signUp layout

        //Get references to all visible buttons in layout
        final Button submitButton =  findViewById(R.id.signup_submit_btn);
        final Button backButton = findViewById(R.id.back_nav);
        final Button passwordSpecBtn = findViewById(R.id.btn_passwordSpec);
        final Button termsBtn = findViewById(R.id.termsLink);

        // Get a reference for the tesxt views that hold sign up info
        final EditText f_name = findViewById(R.id.editFirstName);
        final EditText s_name = findViewById(R.id.editSurname);
        final EditText email = findViewById(R.id.editEmail);
        final EditText password = findViewById(R.id.editPassword);
        final CheckBox agreeCheckBox = findViewById(R.id.TermsCheckBox);



        //define context for post method
        final Context m_context = getApplicationContext();


        //***************************Navigation *******************************
        //When back button clicked navigate to parent activity i.e. login
        backButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), login.class);
                view.getContext().startActivity(intent);}

        });


        //*********************Password Tips and password Validation*********************

        passwordSpecBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
            LayoutInflater inflater = (LayoutInflater) m_context.getSystemService(LAYOUT_INFLATER_SERVICE);
            //PAss in the layout with correct message and the Id of button/label that it is to be positioned above
            CustomPopUpWindow(inflater.inflate(R.layout.password_popup,null), findViewById(R.id.password_label_view));

            }

        });



        //*********************Terms and conditions popo up and checkbox*********************

        termsBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                LayoutInflater inflater = (LayoutInflater) m_context.getSystemService(LAYOUT_INFLATER_SERVICE);

                //PAss in the inflated layout with correct message and the Id of button/label that it is to be positioned above
                CustomPopUpWindow(inflater.inflate(R.layout.terms_and_conditions_pop,null), findViewById(R.id.termsLink));

            }

        });

        agreeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                agree=!agree;       //flips value

            }
        });

        //*************************************************************




        //When Clicked, submit button will validate and then post input
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean allValid= true;

                if (!UserDetailValidation.IsEmailValid(email)) {
                    email.setError(ErrorStrings.getInvalidEmailError());
                    email.setText("");
                    allValid=false;
                }

                if (UserDetailValidation.IsFieldEmpty(f_name.getText().toString()))
                {
                    f_name.setError(ErrorStrings.getEmptyFieldError());
                    f_name.setText("");
                    allValid=false;
                }
                if (UserDetailValidation.IsFieldEmpty(s_name.getText().toString()))
                {
                    s_name.setError(ErrorStrings.getEmptyFieldError());
                    s_name.setText("");
                    allValid=false;
                }

                if (!UserDetailValidation.IsPasswordValid(password))
                {
                    password.setError(ErrorStrings.getPasswordInvalidError());
                    password.setText("");
                    allValid=false;
                }
                if (!agree)
                {
                    allValid = false;
                    Toast.makeText(m_context, ErrorStrings.getTermsNotAgreedError(), Toast.LENGTH_LONG).show();

                }

                //Once all credentials have been internally validated
                if (allValid) {
                    //instantiate user details object with validated field data
                    currentUser = new UserDetails(f_name.getText().toString(), s_name.getText().toString(), email.getText().toString(), password.getText().toString());

                    //Create a JSON object for post request
                    JSONObject userJsonObject;
                    try {
                        userJsonObject = JsonFormater.convertToJsonObj(JsonFormater.convertToJString(currentUser));
                    } catch (Exception ex) {
                        Log.i("Error", ex.toString());
                        Toast.makeText(m_context, ErrorStrings.getUnrecoverableError(), Toast.LENGTH_LONG).show();
                        return; //exit from further request procedure
                    }
                    //Convert to string to post
                    final String userAuthenticateJsonData = userJsonObject.toString();

                    // Instantiate new RequestQueue or get existing queue
                    final RequestQueue queue = VolleyQueue.getInstance(m_context).getRequestQueue();

                    //Build appropriate url address
                    String url = ApiStringConstants.getServerAddress() + ApiStringConstants.getUserAddress();

                    //Send Authenticat Post Request. Call error handler and define how response is read and handled
                    StringRequest authenticationStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY", response);
                            DbHelper databaseHelper= new DbHelper(m_context);       //used to interact with database
                            databaseHelper.AddUser(currentUser.getEmail(), currentUser.getPassword());

                            try {
                                UserDetails tempUserCheck = databaseHelper.checkUserExist();
                                //Create a JSON object for post request
                                JSONObject userAuthorizeJsonObject;
                                try {
                                    //Convert user detail object to json object
                                    userAuthorizeJsonObject = JsonFormater.convertToJsonObj(JsonFormater.convertToJString(tempUserCheck));
                                } catch (Exception ex) {
                                    Log.i("Error", ex.toString());
                                    Toast.makeText(m_context, ErrorStrings.getUnrecoverableError(), Toast.LENGTH_LONG).show();  //display error
                                    Intent intent = new Intent(m_context, login.class);     //Exit back to login Page
                                    startActivity(intent);
                                    return;
                                }
                                //Convert json object to string for post request
                                final String authorizeUserStringJsonData = userAuthorizeJsonObject.toString();

                                //Build API address string
                                String url2 = ApiStringConstants.getServerAddress() + ApiStringConstants.getLogin();

                                //Build Authorisation user request with custom error and response handlers
                                StringRequest authorizationStringRequest = new StringRequest(Request.Method.POST, url2, VolleyCustomResponses.GenerateTokenResponse(m_context), VolleyCustomResponses.GenerateTokenError(m_context))
                                {
                                    @Override
                                    public String getBodyContentType() {
                                        return ApiStringConstants.getCharset();  //How to read response from API
                                    }

                                    //converted string to be sent into bytes. if not volley cant send to server
                                    @Override
                                    public byte[] getBody() {
                                        try {
                                            return authorizeUserStringJsonData.getBytes("utf-8");
                                        } catch (UnsupportedEncodingException uee) {
                                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", authorizeUserStringJsonData, "utf-8");
                                            return null;
                                        }
                                    }

                                    //define how response is read and determine if request was successful or not
                                    @Override
                                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
                                        if (response != null) {
                                            String responseString = new String(response.data);
                                            // can get more details such as response.headers
                                            return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                                        }
                                        return Response.error(new VolleyError());
                                    }
                                };
                                //Add request to queue else return to login activity
                                try {
                                    queue.add(authorizationStringRequest);
                                } catch (Exception ex) {
                                    Log.e("Error", ex.toString());
                                    Toast.makeText(m_context, ErrorStrings.getUnrecoverableError(), Toast.LENGTH_LONG).show();  //display error
                                    Intent intent = new Intent(m_context, login.class);
                                    startActivity(intent);
                                }

                            //Handle internal database exceptions thrown by making users log in
                            } catch (NoStoredUserException ex) {
                                Log.e("Catch", ex.toString());
                                Intent intent = new Intent(m_context, login.class);
                                startActivity(intent);

                            } catch (TooManyUsersException ex) {
                                Log.e("Catch", ex.toString());
                                databaseHelper.DropUserTable();
                                Intent intent = new Intent(m_context, login.class);
                                startActivity(intent);

                            }

                        }
                    }, VolleyCustomResponses.GenerateSignUpError(m_context,email))
                    {   //override functions for authenticate request

                        //How to read response from API
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        //converted string to be sent into bytes. if not volley cant send to server
                        @Override
                        public byte[] getBody() {
                            try {
                                return userAuthenticateJsonData.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", userAuthenticateJsonData, "utf-8");
                                return null;
                            }
                        }
                        //define how response is read and determine if request was successful or not
                        @Override
                        protected Response<String> parseNetworkResponse(NetworkResponse response) {
                            if (response != null) {
                                String responseString = String.valueOf(response.statusCode);
                                // can get more details such as response.headers
                                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                            }
                            return Response.error(new VolleyError());
                        }
                    };

                    //Add request to queue else return to login activity
                    try {
                        queue.add(authenticationStringRequest);
                    } catch (Exception ex) {
                        Log.e("Error", ex.toString());
                        Toast.makeText(m_context, ErrorStrings.getUnrecoverableError(), Toast.LENGTH_LONG).show();  //display error
                        Intent intent = new Intent(m_context, login.class);
                        startActivity(intent);
                    }

                }



            }

        });

    }

    //Function that defines pop up window position and view on UI and also defines button click logic
    private void CustomPopUpWindow(View customView, View layout){
        // Initialize a new instance of popup window
        mPopupWindow = new PopupWindow(
                customView,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT,
                CoordinatorLayout.LayoutParams.WRAP_CONTENT
        );

        // Set an elevation value for popup window
        //Elevation option only for os above 21
        if(Build.VERSION.SDK_INT>=21){
            mPopupWindow.setElevation(5.0f);
        }

        // Get a reference for the custom view close button
        Button closeButton = customView.findViewById(R.id.ib_close);

        // Set a click listener for the popup window close button
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                mPopupWindow.dismiss();
            }
        });
        // Finally, show the popup window at the center location of root relative layout
        mPopupWindow.showAtLocation( layout,Gravity.CENTER,0,0);

    }

}
