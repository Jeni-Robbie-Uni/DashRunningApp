package com.example.dashrunningapp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.EditText;

import androidx.core.app.ActivityCompat;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.example.dashrunningapp.models.geoLocation;
import com.example.dashrunningapp.volley.VolleyCustomResponses;

public class GpsTracker {

    //Location listener
    public LocationListener locationListener;





    //returns geolocation object after determining permissions are in place
    public static geoLocation getLocation(LocationManager locationManager, Context m_context, Activity m_activity) {

        geoLocation myLocation = new geoLocation();

        //Check if user has given persmission to access location
        if (ActivityCompat.checkSelfPermission(
                m_context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                m_context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(m_activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);    //ask for permission
        } else {
            //check which provider is enabled gps
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);  //if so get last known loacation
                if (locationGPS != null) {
                    myLocation.setLongitude(locationGPS.getLongitude()); //  gps location if available
                    myLocation.setLatitude(locationGPS.getLatitude()); // return gps location if available
                    myLocation.setAltitude(locationGPS.getAltitude());
                    return myLocation;
                }
            }
            //if unable to get last gps location use network provider
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (locationNetwork != null) {
                    myLocation.setLongitude(locationNetwork.getLongitude());   // network location as backup
                    myLocation.setLatitude(locationNetwork.getLatitude());   // network location as backup
                    myLocation.setAltitude(locationNetwork.getAltitude());
                    return myLocation;
                }
            }

            return null; //none are available
        }
        return null;
    }

}
