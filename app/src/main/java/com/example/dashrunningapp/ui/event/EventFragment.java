package com.example.dashrunningapp.ui.event;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
import com.example.dashrunningapp.Misc.StringConstants;
import com.example.dashrunningapp.R;
import com.example.dashrunningapp.models.EventDTO;
import com.example.dashrunningapp.volley.VolleyQueue;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.List;

public class EventFragment extends Fragment {


    private static final int REQUEST_LOCATION = 1;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    private GsonBuilder gsonBuilder;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        final Activity m_activity = getActivity();
        ActivityCompat.requestPermissions(m_activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);


        View root = inflater.inflate(R.layout.fragment_event, container, false);
        recyclerView = (RecyclerView) root.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(m_activity);
        recyclerView.setLayoutManager(layoutManager);

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        final Context m_context = getContext();

        //Create a JSON object for post request     //surround in try catch mention in report

        Double doubLatitude = getLatitude(locationManager);
        Double doubLongitude = getLongitude(locationManager);
        String latitude = StringConstants.getBasicLat();
        String longitude = StringConstants.getBasicLong();

        if (doubLatitude !=null  && doubLongitude !=null) {
            try {
                latitude = doubLatitude.toString();
                longitude = doubLongitude.toString();
            } catch (Exception ex) {
                Toast.makeText(m_context, "Cannot determine location", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
        Toast.makeText(m_context, "Cannot determine location", Toast.LENGTH_LONG).show();
        }


            // Instantiate the RequestQueue.
            final RequestQueue queue = VolleyQueue.getInstance(m_context).getRequestQueue();

            String url = StringConstants.getServerAddress() + StringConstants.getEventAddress() + "/" + longitude + "/" + latitude;
            StringRequest stringRequest2 = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {
                    // Get the object Type for a List of EventDTO objects from the system to pass into gson for conversion
                    Type listType = new TypeToken<List<EventDTO>>() {
                    }.getType();
                    Gson gson = new Gson();
                    //converts json sto objects of event type
                    List<EventDTO> list = gson.fromJson(response, listType);

                    //creates empty array of string to list size

                    mAdapter = new EventAdapter(m_context, list);
                    recyclerView.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/json; charset=utf-8";
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

    return root;

    }


    private void OnGPS() {
        final Context m_context = getContext();
        final AlertDialog.Builder builder = new AlertDialog.Builder(m_context);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private Double getLongitude(LocationManager locationManager) {
        final Context m_context = getContext();
        final Activity m_activity = getActivity();

        //Check if user has given persmission to access location
        if (ActivityCompat.checkSelfPermission(
                m_context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                m_context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(m_activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);    //ask for permission
        } else {
            //check which provider is enabled gps or network
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (locationGPS != null) {
                    return locationGPS.getLongitude(); // return gps location if available
                }
            }
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (locationNetwork != null) {
                    return locationNetwork.getLongitude();   //return network location as backup
                }
            }

            return null; //none are available
        }
        return null;
    }

    private Double getLatitude(LocationManager locationManager) {
        final Context m_context = getContext();
        final Activity m_activity = getActivity();

        //Check if user has given persmission to access location
        if (ActivityCompat.checkSelfPermission(
                m_context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                m_context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(m_activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);    //ask for permission
        } else {
            //check which provider is enabled gps or network
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (locationGPS != null) {
                    return locationGPS.getLatitude(); // return gps location if available
                }
            }
            if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Location locationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (locationNetwork != null) {
                    return locationNetwork.getLatitude();   //return network location as backup
                }
            }

            return null; //none are available
        }
        return null;
    }





}





