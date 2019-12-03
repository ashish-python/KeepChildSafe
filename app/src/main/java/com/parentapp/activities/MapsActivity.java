package com.parentapp.activities;

import androidx.annotation.ColorInt;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parentapp.constants.Endpoints;
import com.parentapp.listeners.BaseListener;
import com.parentapp.stores.TokenStore;
import com.parentapp.utils.NetworkPostRequest;
import com.parentapp.utils.SpinnerAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, BaseListener {

    private GoogleMap mMap;
    private TokenStore tokenStore;
    @ColorInt
    public static final int CIRCLE_RED_FILL = 0x55D81B60;
    private static final String FCM_TOKEN = "e0MYi8OKPTU:APA91bGry9RdnzZ9ihQ-w1mSGWQKOrRdhOLuO5mZaWzm6ddWSWOxsCjCxNJXfqvBh9rDbFCr1qiXeKvfhk6iyT3ZgyUYnpbtaZpopc3cSl1UBphmw6665hqsJqzBhQgsCN-ll42srZ0d";
    private boolean mapOpen = false;
    private JSONArray geofencesArray = null;
    private HashMap<String, JSONObject> geofencesMap;
    private Spinner spinner;
    private Button trackBtn;
    private ArrayList<String> childIdList;
    private ArrayList<String> childNameList;
    private String selectedChildId;
    private volatile static boolean track = false;
    private Marker locationMarker;
    private Handler handler = new Handler();
    private int lastSpinnerPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        initViews();
        setListeners();
        tokenStore = TokenStore.getInstance(getApplicationContext());
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geofencesMap = new HashMap<>();
        childIdList = new ArrayList<>();
        childNameList = new ArrayList<>();
    }

    private void initViews() {
        spinner = findViewById(R.id.spinner);
        trackBtn = findViewById(R.id.track_btn);
    }

    private void setListeners() {
        trackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!MapsActivity.track) {
                    startLocationTracking();
                } else {
                    stopLocationTracking();
                }
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        String fcm_token = tokenStore.getFCMToken();
        getParentData();
        mMap = googleMap;
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (MapsActivity.track) {
            Log.v("FCM_PAUSE", "PAUSE");
            stopLocationTracking();
        }
    }

    //This method makes a POST request to a script on the server to send a push notification to the child to start sending location information
    private void handleLocationSharingRequest(String task) {
        new NetworkPostRequest(getApplicationContext(), Endpoints.TRACK_LOCATION_NOTIFICATION_URL, this::callback2, Endpoints.REQUEST_LOCATION_TASK).execute(childIdList.get(lastSpinnerPosition), TokenStore.getInstance(getApplicationContext()).getUser(), task);
    }

    private void getChildLocation(String task) {
        new NetworkPostRequest(getApplicationContext(), Endpoints.GET_CHILD_LOCATION_URL, this::callback, Endpoints.GET_CHILD_LOCATION_TASK).execute(childIdList.get(lastSpinnerPosition), TokenStore.getInstance(getApplicationContext()).getUser());
    }

    private void startLocationTracking() {
        MapsActivity.track = true;
        trackBtn.setText(R.string.stop_tracking);
        handleLocationSharingRequest(Endpoints.SEND_LOCATION_REQUEST);
        new Thread(new Runnable() {
            @Override
            public void run() {
                int i = 0;
                while (MapsActivity.track) {
                    getChildLocation(Endpoints.GET_CHILD_LOCATION_TASK);
                    try {
                        Thread.sleep(2000);
                        Log.v("FCM_START", String.valueOf(i++));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void stopLocationTracking() {
        //stop retrieving location information from the server
        MapsActivity.track = false;
        Log.v("FCM_STOP", "STOP");
        trackBtn.setText(R.string.track);
        //Tell the child phone to stop tracking location information
        handleLocationSharingRequest(Endpoints.STOP_LOCATION_REQUEST);
    }

    public void callback2(Context context, Integer status, String responseString) {
        Log.v("FCM_LOC_REQ_RESP", responseString);
    }

    //This callback method is called after a push notification is sent to the child to start or stop sending location
    //If the request is to send location then start a loop  that retrieves last known location every second and displays it on the map
    @Override
    public void callback(Context context, Integer status, String responseString) {
        Log.v("FCM_CALLBACK", responseString);
        if (!responseString.equals("fail")) {
            try {
                JSONObject jsonObject = new JSONObject(responseString);

                Double lat = jsonObject.getDouble("lastKnownLat");
                Double lng = jsonObject.getDouble("lastKnownLng");

                if (locationMarker == null) {
                    locationMarker = mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng))
                            .title("location")
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                } else {

                    locationMarker.setPosition(new LatLng(lat, lng));
                }
                // mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(40.743609, -74.0270757), 15));
            } catch (JSONException e) {
                Log.v("FCM_ERR", e.toString());
            }
        }
    }

    private void getParentData() {
        new NetworkPostRequest(MapsActivity.this, Endpoints.GET_PARENT_DATA_URL, this::showGeofences, Endpoints.GET_PARENT_DATA).execute(TokenStore.getInstance(getApplicationContext()).getUser());
    }

    //A callback method called after information of the parent and the kids is fetched from the server
    @SuppressLint("StaticFieldLeak")
    public void showGeofences(Context context, Integer status, String json) {
        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (jsonObject.has("children")) {
                        JSONArray childrenArray = jsonObject.getJSONArray("children");
                        for (int i = 0; i < childrenArray.length(); i++) {
                            if (childrenArray.getJSONObject(i).has("registeredGeofences")) {
                                geofencesMap.put(childrenArray.getJSONObject(i).getString("id"), childrenArray.getJSONObject(i));
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "fail";
                }
                return "success";
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                Log.v("FCM_STATUS", s);
                int i = 0;
                if (s.equals("success")) {
                    if (geofencesMap.size() > 0) {
                        //Populate two ArrayList with child id and child name
                        for (String key : geofencesMap.keySet()) {
                            childIdList.add(key);
                            try {
                                childNameList.add(geofencesMap.get(key).getString("childFirstName"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        //Add name of children to Spinner
                        SpinnerAdapter adapter = new SpinnerAdapter(MapsActivity.this, childNameList);
                        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        spinner.setAdapter(adapter);

                        //OnItemSelectedListener for the spinner
                        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                                if (lastSpinnerPosition != position) {
                                    lastSpinnerPosition = position;
                                    stopLocationTracking();
                                }
                                drawGeofences(position);
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> adapterView) {

                            }
                        });
                    }
                }
            }
        }.execute();
    }

    /**
     * This method draws geofences for the child selected in the dropdown
     *
     * @param position is the index in the drop down
     */
    private void drawGeofences(int position) {
        selectedChildId = childIdList.get(position);
        JSONObject childObj = geofencesMap.get(selectedChildId);
        try {
            JSONArray geofenceArray = childObj.getJSONArray("registeredGeofences");
            mMap.clear();
            Log.v("FCM_GEO_LENGTH", String.valueOf(geofenceArray.length()));
            for (int i = 0; i < geofenceArray.length(); i++) {
                JSONObject obj = geofenceArray.getJSONObject(i);
                LatLng latLng = new LatLng(obj.getDouble("lat"), obj.getDouble("lng"));
                Log.v("FCM_GEO_LENGTH", obj.getString("geofenceName"));
                mMap.addMarker(new MarkerOptions().position(latLng).title(obj.getString("geofenceName")));
                mMap.addCircle(new CircleOptions()
                        .center(latLng)
                        .radius(obj.getInt("radius"))
                        .strokeColor(Color.RED)
                        .fillColor(CIRCLE_RED_FILL));
                if (i == 0) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15
                    ));
                }
            }
            trackBtn.setVisibility(View.VISIBLE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
