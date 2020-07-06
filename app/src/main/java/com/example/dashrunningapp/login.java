package com.example.dashrunningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;


public class login extends AppCompatActivity {

UserDetails current_user=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Button signUpBtn = (Button) findViewById(R.id.SignUpLink);
        Button loginBtn = (Button) findViewById(R.id.loginBtn);



        //define context for post method
        final Context m_context = getApplicationContext();





        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), SignUp.class);
                view.getContext().startActivity(intent);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText email = findViewById(R.id.editEmail);
                EditText password = findViewById(R.id.editPassword);

                boolean allValid= true;

                if (!UserDetailValidation.IsEmailValid(email)) {
                    email.setError("Invalid email. Needs to be an email fam e.g. example@gmail.com");
                    email.setText("");
                    allValid=false;
                }

                if (UserDetailValidation.IsFieldEmpty(password.getText().toString()))
                {
                    password.setError("@String/EmptyField");
                    password.setText("");
                    allValid=false;
                }

                if (allValid){

                    //Create instance of user deta
                    current_user = new UserDetails(email.getText().toString(), password.getText().toString());

                    //Create a JSON object for post request
                    JSONObject userJsonObject = null;
                    try {
                        userJsonObject = JsonFormater.convertToJsonObj(JsonFormater.convertToJString(current_user));
                    } catch (Exception ex) {
                        Log.i("Error", ex.toString());
                    }

                    final String jsonData = userJsonObject == null ? null : userJsonObject.toString();

                    // Instantiate the RequestQueue.
                    final RequestQueue queue = VolleyQueue.getInstance(m_context).getRequestQueue();

                    String url = StringConstants.getServerAddress() + StringConstants.getLogin();
                    StringRequest stringRequest2 = new StringRequest(Request.Method.POST, url, VolleyCustomResponses.GenerateTokenResponse(m_context, current_user), VolleyCustomResponses.GenerateTokenError(m_context))
                    {
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
                            if (response != null) {
                                String responseString = new String(response.data);
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




                }

            }
        });


    }


}
