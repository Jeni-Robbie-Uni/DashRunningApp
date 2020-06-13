package com.example.dashrunningapp;

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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

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

            }




        });


    }



    RequestQueue queue = Volley.newRequestQueue(this);




}
