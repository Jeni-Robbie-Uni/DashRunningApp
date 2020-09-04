package com.example.dashrunningapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.dashrunningapp.R;
import com.example.dashrunningapp.SQLiteDb.DbHelper;
import com.google.android.material.navigation.NavigationView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


//Main activity class is responsible for managing navigation between fragments
public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_nav);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //assigning layouts to object
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Builds configuratoin used by controller to create navigation drawer menu in app bar
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_track, R.id.nav_event, R.id.nav_profile)
                .setDrawerLayout(drawer)
                .build();
        //get navigation controller
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
       //build nav menu using appbar config
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the side setting and adds items to the action bar if its present
        getMenuInflater().inflate(R.menu.side_menu, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        //deals with back track navigation- always returns to home fragment
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection embedded in switchcase for other items to be added later in development
        switch (item.getItemId()) {
            case R.id.action_logout:
                DbHelper databaseHelper = new DbHelper(MainActivity.this);      //used to interact with database
                databaseHelper.DropUserTable();
                Intent intentLogin = new Intent(this, login.class); //open login page
                startActivity(intentLogin);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}