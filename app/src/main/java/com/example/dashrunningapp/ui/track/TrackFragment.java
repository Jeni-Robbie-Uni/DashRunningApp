package com.example.dashrunningapp.ui.track;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.dashrunningapp.GpsTracker;
import com.example.dashrunningapp.Misc.ErrorStrings;
import com.example.dashrunningapp.Misc.MiscStringConstants;
import com.example.dashrunningapp.R;
import com.example.dashrunningapp.models.RunStats;
import com.example.dashrunningapp.models.geoLocation;

import java.text.DecimalFormat;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class TrackFragment extends Fragment {
    private PopupWindow mPopupWindow;
    boolean isNotPlaying = true;  //holds status of play button
    boolean isUserFinished = false;      //holds popup window response
    long time = 0;    //variable that holds time to remove to reset chrometer or value ot at at start
    boolean resetStartLocation= true;
    int count=0;
    RunStats currentRun = new RunStats();
    //Create instance of location manager
    LocationManager locationManager;
    Double latOld, longOld, altitudeOld;
    geoLocation startLocation;

    LocationListener myListener;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        //get activity context
        final Context m_context = getActivity();
        View root = inflater.inflate(R.layout.fragment_track, container, false);

        // Get a reference for the tesxt views, buttons and chrometer
        final Button btn_play = root.findViewById(R.id.btn_play);
        final TextView txt_play = root.findViewById(R.id.trackLabel);
        final TextView txt_distance = root.findViewById(R.id.Distance);
        final TextView txt_elevation = root.findViewById(R.id.Elevation);
        final Chronometer timer = root.findViewById(R.id.stopWatch);

        txt_distance.setText("0km");
        txt_elevation.setText("0m");



        //vcreate custom listener response
        myListener= new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                float[] results = new float[1]; //next method expects array bakka
                Location.distanceBetween(latOld, longOld, location.getLatitude(), location.getLongitude(), results);
                currentRun.setTotalDistance((currentRun.getTotalDistance() + results[0]));

                currentRun.setElevationDistance(currentRun.getElevationDistance());
                DecimalFormat df = new DecimalFormat();
                df.setMaximumFractionDigits(2);
                String totDistanceString= df.format(currentRun.getTotalDistance()/1000);
                txt_distance.setText(totDistanceString + "km");
                txt_elevation.setText(String.valueOf(currentRun.getElevationDistance()));

                latOld=location.getLatitude();
                longOld=location.getLongitude();
                altitudeOld=location.getAltitude();
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };







        //play button listener logic
        btn_play.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {


                //when play btn is clicked when in "static" state
                if (isNotPlaying) {
                    if (resetStartLocation){
                        //get start location
                        startLocation= GpsTracker.getLocation(locationManager, m_context, getActivity());

                        if (startLocation == null){
                            Toast.makeText(m_context, ErrorStrings.getLoctaionNotFoundError(), Toast.LENGTH_LONG).show();
                        }
                        latOld= startLocation.getLatitude();
                        longOld=startLocation.getLongitude();
                        altitudeOld= startLocation.getAltitude();

                    }
                    //Change btn image and text
                    btn_play.setBackgroundResource(R.drawable.ic_stop);
                    txt_play.setText(MiscStringConstants.getStop());

                    isNotPlaying = !isNotPlaying; //revert to opposite
                    //set the chrometer base to start at specific time
                    timer.setBase(SystemClock.elapsedRealtime() + time);
                    timer.start();

                    //check permissions
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        if (ActivityCompat.checkSelfPermission(m_context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(m_context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);    //ask for permission
                        }
                        else {
                            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0,myListener);
                        }
                    }


               }
                //when play button is clicked in "running" state
               else{
                    // gets timer base minus sytem elapsed time to ensure chrometer restarts at correct time
                   time= timer.getBase()- SystemClock.elapsedRealtime();
                   timer.stop();
                   locationManager.removeUpdates(myListener);
                   resetStartLocation=true;

                    LayoutInflater inflater = (LayoutInflater) m_context.getSystemService(LAYOUT_INFLATER_SERVICE);

                   //PAss in the layout to be inflated to pop up window function
                   CustomPopUpWindow(inflater.inflate(R.layout.stoprun_popup,null), btn_play);

                   //set pop up window dismiss listener to execut timer logic
                   mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                       @Override
                       public void onDismiss() {
                          if (isUserFinished){
                              //run is finished and timer is stopped
                              btn_play.setVisibility(View.INVISIBLE);
                              txt_play.setVisibility(View.INVISIBLE);
                          }
                          else{
                              //play icon appears and users have to resume
                              btn_play.setBackgroundResource(R.drawable.ic_play);
                              txt_play.setText(MiscStringConstants.getResume());
                              isNotPlaying =!isNotPlaying;  //revert boolean

                          }
                       }
                   });

               }
            }

        });

        return root;

    }

    //Function that defines pop up window position and view on UI and also defines button click logic
    private void CustomPopUpWindow(View customView, View rLayout){
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

        // Get a reference for the custom view  buttons
        Button btn_resume = customView.findViewById(R.id.btn_continue);
        Button btn_finished = customView.findViewById(R.id.btn_finsihed);
        // Set a click listener for the popup window close button
        btn_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                isUserFinished=false;
                mPopupWindow.dismiss();
            }
        });
        btn_finished.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Dismiss the popup window
                isUserFinished=true;
                mPopupWindow.dismiss();
            }
        });

        // Finally, show the popup window at the center location of root relative layout
        mPopupWindow.showAtLocation( rLayout, Gravity.CENTER,0,0);

    }

}
