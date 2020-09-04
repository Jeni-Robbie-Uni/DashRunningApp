package com.example.dashrunningapp.ui.event;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.example.dashrunningapp.Misc.ApiStringConstants;
import com.example.dashrunningapp.Misc.ErrorStrings;
import com.example.dashrunningapp.Misc.MiscStringConstants;
import com.example.dashrunningapp.R;
import com.example.dashrunningapp.models.EventDTO;
import com.example.dashrunningapp.models.geoLocation;
import com.example.dashrunningapp.volley.VolleyQueue;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class EventFragment extends Fragment {


    private static final int REQUEST_LOCATION = 1;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;



    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        final Activity m_activity = getActivity();

        //get permission request from manifest
        ActivityCompat.requestPermissions(m_activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        View root = inflater.inflate(R.layout.fragment_event, container, false);
        //set reference to event frag recycler view
        recyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view);

        // only doing 10 events so this will help performance
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(m_activity);
        recyclerView.setLayoutManager(layoutManager);

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        final Context m_context = getContext();

        //get longitude latitude geo location
        geoLocation myLocation = getLocation(locationManager);
        //assign backup values to strings so uk location still sent
        String latitude = MiscStringConstants.getBasicLat();
        String longitude = MiscStringConstants.getBasicLong();

        //convert geolocation long and lat to string
        if (myLocation != null) {
            try {
                latitude = String.valueOf(myLocation.getLatitude());
                longitude = String.valueOf(myLocation.getLongitude());
            } catch (Exception ex) {
                Toast.makeText(m_context, ErrorStrings.getLoctaionNotFoundError(), Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            //error message and request carries on with default values
            Toast.makeText(m_context, ErrorStrings.getLoctaionNotFoundError(), Toast.LENGTH_LONG).show();
        }


        // Instantiate the RequestQueue or return exiting request queue
        final RequestQueue queue = VolleyQueue.getInstance(m_context).getRequestQueue();

        //generate server address to target request
        String url = ApiStringConstants.getServerAddress() + ApiStringConstants.getEventAddress() + "/" + longitude + "/" + latitude;

         //create get request to api for events near loaction
        //change to post when time as sensitive info
        StringRequest eventsRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Get the object Type for a List of EventDTO objects from the system to pass into gson for conversion
                Type listType = new TypeToken<List<EventDTO>>() {}.getType();
                Gson gson = new Gson();
                //converts json objects of event type
                List<EventDTO> list = gson.fromJson(response, listType);


                mAdapter = new EventAdapter(m_context, list);
                recyclerView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
            }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(m_context, ErrorStrings.getUnrecoverableError(), Toast.LENGTH_LONG).show();
                }
            }) {
            //converted string to be sent into bytes. if not volley cant send to server
                @Override
                public String getBodyContentType() {
                    return ApiStringConstants.getCharset();
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
            //events request to volley request queue
            try {
                queue.add(eventsRequest);
            } catch (Exception ex) {
                Log.e("Error", ex.toString());
            }

    return root;

    }

//returns geolocation object after determining permissions are in place
    private geoLocation getLocation(LocationManager locationManager) {
        final Context m_context = getContext();
        final Activity m_activity = getActivity();
        geoLocation myLocation = new geoLocation();

        //Check if user has given persmission to access location
        if (ActivityCompat.checkSelfPermission(
                m_context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                m_context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(m_activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);    //ask for permission
        } else {
            //check which provider is enabled gps
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);  //if so get last known loacation
                if (locationGPS != null) {
                    myLocation.setLongitude(locationGPS.getLongitude()); //  gps location if available
                    myLocation.setLatitude(locationGPS.getLatitude()); // return gps location if available
                    return myLocation;
                }
            }
            //if unable to get last gps location use network provider
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (locationNetwork != null) {
                    myLocation.setLongitude(locationNetwork.getLongitude());   // network location as backup
                    myLocation.setLatitude(locationNetwork.getLatitude());   // network location as backup
                }
            }

            return null; //none are available
        }
        return null;
    }





}





