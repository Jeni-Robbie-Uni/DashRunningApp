package com.example.dashrunningapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;



public class SplashScreen extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Intent intent = new Intent(this, MainActivity.class);

        startActivity(intent);

        // Following the documentation, right after starting the activity
        // we override the transition
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}
