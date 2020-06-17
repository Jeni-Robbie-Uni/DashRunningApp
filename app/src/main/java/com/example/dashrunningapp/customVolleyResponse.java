package com.example.dashrunningapp;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;

public class customVolleyResponse {

    public customVolleyResponse(VolleyError error, EditText text, Context x){
            if (error.networkResponse.statusCode==409){
                text.setError("Account Already Exists");
            }else{

                Toast.makeText(x, error.toString(), Toast.LENGTH_LONG).show();
            }

    }

}
