package com.example.dashrunningapp;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;


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


import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;




public class SignUp extends AppCompatActivity {

    private PopupWindow mPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);      //display signup layout

        //Get references to all visible buttons in layout
        final Button submitButton =  findViewById(R.id.signup_submit_btn);
        final Button backButton = findViewById(R.id.back_nav);
        final Button passwordSpecBtn = findViewById(R.id.btn_passwordSpec);

        // Get a reference for the tesxt views that hold sign up info
        final EditText f_name = findViewById(R.id.editFirstName);
        final EditText s_name = findViewById(R.id.editSurname);
        final EditText email = findViewById(R.id.editEmail);
        final EditText password = findViewById(R.id.editPassword);

        //define context for post method
        final Context m_context = getApplicationContext();





        //***************************Navigation *******************************
        //When back button clicked navigate to parent activity i.e. login
        backButton.setOnClickListener(new View.OnClickListener() {
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
                    // Inflate the custom layout/view
                    View customView = inflater.inflate(R.layout.pop_layout,null);

                    // Initialize a new instance of popup window
                    mPopupWindow = new PopupWindow(
                            customView,
                            CoordinatorLayout.LayoutParams.WRAP_CONTENT,
                            CoordinatorLayout.LayoutParams.WRAP_CONTENT
                    );

                    // Set an elevation value for popup window
                    // Call requires API level 21
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
                    View rLayout= findViewById(R.id.password_label_view);
                    mPopupWindow.showAtLocation( rLayout,Gravity.CENTER,0,0);
                }

            });


            //*************************************************************











            //When Clicked, submit button will validate and then post input
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {




                boolean allValid= true;



                    if (!UserDetailValidation.IsEmailValid(email)) {
                        email.setError("Invalid email. Needs to be an email fam e.g. example@gmail.com");
                        email.setText("");
                        allValid=false;
                    }

                    if (UserDetailValidation.IsFieldEmpty(f_name.getText().toString())){
                    f_name.setError("@String/EmptyField");
                    f_name.setText("");
                    allValid=false;
                }
                if (UserDetailValidation.IsFieldEmpty(s_name.getText().toString())){
                    s_name.setError("@String/EmptyField");
                    s_name.setText("");
                    allValid=false;
                }

                if (!UserDetailValidation.IsPasswordValid(password, 13,5)){
                    password.setError("@String/invalid_password");
                    password.setText("");
                    allValid=false;
                }




                if (allValid) {
                    //Create instance of user deta
                    final UserDetails current_user = new UserDetails(f_name.getText().toString(), s_name.getText().toString(), email.getText().toString(), password.getText().toString());


                    //Create a JSON object for post request
                    JSONObject userJsonObject = null;
                    try {
                        userJsonObject = JsonFormater.convertToJsonObj(JsonFormater.convertToJString(current_user));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    final String jsonData = userJsonObject == null ? null : userJsonObject.toString();

                    // Instantiate the RequestQueue.
                    final RequestQueue queue = VolleyQueue.getInstance(m_context).
                            getRequestQueue();

                    final String url = "http://localhost:5000" + "/api/Users";
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.i("VOLLEY", response);







                            DbHelper databaseHelper= new DbHelper(m_context);
                            databaseHelper.AddUser(current_user.getEmail(), current_user.getPassword());






                            try {
                                UserDetails tempUserCheck = databaseHelper.checkUserExist();
                                //Create a JSON object for post request
                                JSONObject userJsonObject2= null;
                                try {
                                    userJsonObject2 = JsonFormater.convertToJsonObj(JsonFormater.convertToJString(tempUserCheck));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                final String jsonData2 = userJsonObject2 == null ? null : userJsonObject2.toString();



                                //Authorise with API
                                String url2 = "http://localhost:5000" + "/api/Login";
                                StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url2, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        Log.i("VOLLEY", response);
                                        AuthenticationDetails x = AuthenticationDetails.getInstance();
                                        AuthenticationDetails.setToken(response);

                                        Intent intent = new Intent(m_context, MainActivity.class);
                                        startActivity(intent);
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
                                }){
                                    @Override
                                    public String getBodyContentType() {
                                        return "application/json; charset=utf-8";
                                    }

                                    //cant convert bytes= volley cant send to server i.e cause its custom request or corript
                                    @Override
                                    public byte[] getBody() {
                                        try {
                                            return jsonData2 == null ? null : jsonData2.getBytes("utf-8");
                                        } catch (UnsupportedEncodingException uee) {
                                            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", jsonData, "utf-8");
                                            return null;
                                        }
                                    }
                                    @Override
                                    protected Response<String> parseNetworkResponse(NetworkResponse response) {
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
                                    queue.add(stringRequest2);
                                } catch (Exception ex) {
                                    Log.e("Error", ex.toString());
                                }

                            } catch (NoStoredUserException e) {
                                e.printStackTrace();
                            } catch (TooManyUsersException e) {
                                e.printStackTrace();

                            }



















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
                                if (error.networkResponse.statusCode == 409) {
                                    email.setText("");
                                    email.setError("waaaaaaaaaaah");

                                } else {
                                    Toast.makeText(m_context,
                                            "servererror",
                                            Toast.LENGTH_LONG).show();
                                }
                            } else if (error instanceof NetworkError) {
                                //TODO
                                Toast.makeText(m_context,
                                        "networkerror",
                                        Toast.LENGTH_LONG).show();
                            } else if (error instanceof ParseError) {
                                //TODO
                            }
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
                                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
                            }
                            return Response.error(new VolleyError());

                        }
                    };


                    try {
                        queue.add(stringRequest);
                    } catch (Exception ex) {
                        Log.e("Error", ex.toString());
                    }


                }



            }





        });

    }

}
