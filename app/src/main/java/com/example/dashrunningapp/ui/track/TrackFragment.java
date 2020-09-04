package com.example.dashrunningapp.ui.track;

import android.annotation.SuppressLint;
import android.content.Context;
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
import androidx.fragment.app.Fragment;

import com.example.dashrunningapp.R;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class TrackFragment extends Fragment {
    private PopupWindow mPopupWindow;
    boolean play=true;
    boolean isUserFinished= true;

    long time=0;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);



        final Context m_context= getActivity();

        View root = inflater.inflate(R.layout.fragment_track, container, false);

        final Button btn_play = root.findViewById(R.id.btn_play);

        final TextView txt_play=root.findViewById(R.id.trackLabel);
        final Chronometer timer= root.findViewById(R.id.stopWatch);





        btn_play.setOnClickListener(new View.OnClickListener()
        {

            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View view) {
               if (play) {

                   btn_play.setBackgroundResource(R.drawable.ic_stop);
                   txt_play.setText("Stop");
                   play=false;
                   timer.setBase(SystemClock.elapsedRealtime()+time);
                    timer.start();
               }

               else{

                   time= timer.getBase()- SystemClock.elapsedRealtime();
                   timer.stop();
                   LayoutInflater inflater = (LayoutInflater) m_context.getSystemService(LAYOUT_INFLATER_SERVICE);
                   //PAss in the layout with correct message and the Id of button/label that it is to be positioned above
                   CustomPopUpWindow(inflater.inflate(R.layout.stoprun_popup,null), btn_play);

                   mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                       @Override
                       public void onDismiss() {
                          if (isUserFinished){
                              btn_play.setVisibility(View.INVISIBLE);
                              txt_play.setVisibility(View.INVISIBLE);
                          }
                          else{
                              btn_play.setBackgroundResource(R.drawable.ic_play);
                              txt_play.setText("Resume");
                              play=true;

                          }
                       }
                   });

               }
            }

        });

        return root;

    }

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

        // Get a reference for the custom view close button
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
    /*
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 0; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 0;

    // methods omitted

    LocationListener myLocationListener = new LocationListener()
    {
        public void onLocationChanged(Location loc2)
        {
            Toast.makeText(MainActivity.this,
                    "Location has changed",
                    Toast.LENGTH_LONG).show();
            if(loc2 != null)
            {
                latitude.setText("Current Latitude: " + String.valueOf(loc2.getLatitude()));
                longitude.setText("Current Longitude: " + String.valueOf(loc2.getLongitude()));
                float[] results = new float[1];
                Location.distanceBetween(lat1, lon1, loc2.getLatitude(), loc2.getLongitude(), results);
                System.out.println("Distance is: " + results[0]);

                dist += results[0];
                DecimalFormat df = new DecimalFormat("#.##"); // adjust this as appropriate
                distanceText.setText(df.format(dist));

                Toast.makeText(MainActivity.this,
                        "Distance",
                        Toast.LENGTH_LONG).show();
                lat1=loc2.getLatitude();
                lon1=loc2.getLongitude();
            }
        }
        */


}
