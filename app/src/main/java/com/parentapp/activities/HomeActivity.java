package com.parentapp.activities;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.parentapp.constants.Endpoints;
import com.parentapp.geofencessdk.GeofenceEvent;
import com.parentapp.listeners.BaseListener;
import com.parentapp.listeners.OnSwipeTouchListener;
import com.parentapp.stores.PermissionStore;
import com.parentapp.stores.TokenStore;
import com.parentapp.utils.NetworkPostRequest;
import com.parentapp.utils.RecyclerViewAdapter;
import com.parentapp.utils.SpinnerAdapter;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class HomeActivity extends BaseAppCompatActivity implements BaseListener {

    private ImageView settingsButton;
    private boolean isHomeFullyVisible = true;
    private FrameLayout homeLayout;
    private float homeTranslationDistance;
    private FrameLayout topLayout;
    private ImageView closeButton;
    private TextView trackChild;
    private TextView signOutTV;
    private FrameLayout permissionLayout;
    private TextView textViewMessage;
    private Button locationPermissionBtn;
    private RelativeLayout settingsLayout;
    private Spinner childSpinner;
    private int lastSpinnerPosition = 0;
    private String selectedChildId;
    private ArrayList<String> childIdList;
    private ArrayList<String> childNameList;
    private HashMap<String, JSONObject> childrenMap;

    private String firstName;
    private String lastName;
    private JSONArray geofencesArray;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter recyclerViewAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private static int LOCATION_PERMISSION_CODE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        initLists();
        setupClickListeners();
        setupSwipeListener();
        setHomeTranslationDistance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //showSuccessMessage();
        setFCMToken();
        closeSettingsPane();
        //get ids and first name for all the children
        getChildrenNames();
    }

    private void initViews() {
        settingsButton = findViewById(R.id.settings_button);
        homeLayout = findViewById(R.id.home_layout);
        topLayout = findViewById(R.id.top_layout);
        closeButton = findViewById(R.id.close_button);
        trackChild = findViewById(R.id.track_child);
        permissionLayout = findViewById(R.id.permission_layout);
        textViewMessage = findViewById(R.id.textview_message);
        locationPermissionBtn = findViewById(R.id.button_permission);
        signOutTV = findViewById(R.id.sign_out);
        settingsLayout = findViewById(R.id.settings_layout);
        childSpinner = findViewById(R.id.child_spinner);
        recyclerView = findViewById(R.id.recyclerview);
    }

    private void initLists() {
        childrenMap = new HashMap<>();
        childIdList = new ArrayList<>();
        childNameList = new ArrayList<>();
    }

    private void setupClickListeners() {
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isHomeFullyVisible) {
                    openSettingsPane();
                } else {
                    closeSettingsPane();
                }
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSettingsPane();
            }
        });

        trackChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMap();
            }
        });

        signOutTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TokenStore.getInstance(getApplicationContext()).setUser("");
                startActivity(HomeActivity.this, MainActivity.class, FINISH_CURRENT_ACTIVITY);
            }
        });

        locationPermissionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestLocationPermission();
            }
        });
    }

    private void showMap() {
        startActivity(this, MapsActivity.class, FINISH_NO_ACTIVITY);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupSwipeListener() {
        topLayout.setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                closeSettingsPane();
            }
        });
    }

    private void setFCMToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {

                        }
                        // Get Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        TokenStore tokenStore = TokenStore.getInstance(getApplicationContext());
                        if (!token.equals(tokenStore.getFCMToken())) {
                            tokenStore.setFCMToken(token);
                            parentUpdateFCMToken(token);
                        }
                        Log.v("FCM_TOKEN: ", token);
                        parentUpdateFCMToken(token);
                    }
                });
    }

    private void parentUpdateFCMToken(String newFCMToken) {
        new NetworkPostRequest(HomeActivity.this, Endpoints.PARENT_FCM_TOKEN_UPDATE_URL, this::callback, Endpoints.PARENT_UPDATE_FCM_TOKEN).execute(TokenStore.getInstance(getApplicationContext()).getUser(), newFCMToken);
    }

    @Override
    public void callback(Context context, Integer status, String responseString) {
        Log.v("FCM_UPDATE", responseString);
        //The FCM Token has been updated so get children data again
        getChildrenNames();
    }

    private void setHomeTranslationDistance() {
        Display display = getWindowManager().getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        homeTranslationDistance = (int) -(displaySize.x * 0.65);
    }

    private void openSettingsPane() {
        topLayout.setVisibility(View.VISIBLE);
        homeLayout.animate().x(homeTranslationDistance).start();
        isHomeFullyVisible = false;
    }

    private void closeSettingsPane() {
        homeLayout.animate().x(0).y(0).z(0).start();
        isHomeFullyVisible = true;
        topLayout.setVisibility(View.GONE);
    }

    //Check if the user has allowed location tracking
    //This method is not being used currently
    private boolean checkLocationPermission() {
        //location permission granted
        if (ContextCompat.checkSelfPermission(HomeActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            permissionLayout.setVisibility(View.GONE);
            homeLayout.setVisibility(View.VISIBLE);
            settingsLayout.setVisibility(View.VISIBLE);
            return true;
        }
        permissionLayout.setVisibility(View.VISIBLE);
        homeLayout.setVisibility(View.GONE);
        settingsLayout.setVisibility(View.GONE);
        if (!PermissionStore.getInstance(this).getLocationPermissionAskedOnce()) {
            textViewMessage.setText(R.string.location_request_message);
            locationPermissionBtn.setVisibility(View.VISIBLE);
            PermissionStore.getInstance(this).setLocationPermissionAskedOnce(true);
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
            textViewMessage.setText(R.string.location_request_message);
            locationPermissionBtn.setVisibility(View.VISIBLE);
        } else {
            textViewMessage.setText(R.string.location_do_not_show_again);
            locationPermissionBtn.setVisibility(View.INVISIBLE);
        }
        return false;
    }

    //Open location permission dialog
    //Not being used currently
    private void requestLocationPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_CODE);
    }

    //This method is called when the user denies or accepts location tracking
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == LOCATION_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                textViewMessage.setText(R.string.welcome_message);
                locationPermissionBtn.setVisibility(View.GONE);
            } else {
                //if the user has clicked on Don't Ask Again
                if (!ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    textViewMessage.setText(R.string.location_do_not_show_again);
                    locationPermissionBtn.setVisibility(View.INVISIBLE);
                } else {
                    textViewMessage.setText(R.string.location_request_message);
                    locationPermissionBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                }

            }

        }
    }

    private void showSuccessMessage() {
        //textViewMessage.setText(R.string.welcome_message);
        locationPermissionBtn.setVisibility(View.GONE);
    }

    private void getChildrenNames() {
        Log.v("FCM_BASIC_ASK", "ASK FOR CHILD DATA");
        new NetworkPostRequest(HomeActivity.this, Endpoints.GET_ALL_CHILDREN_URL, this::callback2, Endpoints.GET_ALL_CHILDREN_TASK).execute(TokenStore.getInstance(getApplicationContext()).getUser(), TokenStore.getInstance(getApplicationContext()).getFCMToken());
    }

    //Fill the spinner with the names of the children
    private void callback2(Context context, Integer status, String responseString) {
        Log.v("FCM_CHILDREN_NAMES", responseString);
        if (!responseString.equals("fail")) {
            //Fill Spinner here
            childNameList.clear();
            childIdList.clear();
            try {
                JSONArray childrenArray = new JSONArray(responseString);
                for (int i = 0; i < childrenArray.length(); i++) {
                    childIdList.add(childrenArray.getJSONObject(i).getString("id"));
                    childNameList.add(childrenArray.getJSONObject(i).getString("name"));
                }

                SpinnerAdapter adapter = new SpinnerAdapter(HomeActivity.this, childNameList);
                //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                childSpinner.setAdapter(adapter);

                //OnItemSelectedListener for the spinner
                childSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                        if (lastSpinnerPosition != position) {
                            lastSpinnerPosition = position;
                        }
                        //Display the child's event history when selected from the dropdown
                        getChildEventHistory(TokenStore.getInstance(getApplicationContext()).getUser(), TokenStore.getInstance(getApplicationContext()).getFCMToken(), childIdList.get(position));
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            } catch (JSONException e) {
                Log.v("FCM_BASIC_JSON", "JSON_ERROR");
                e.printStackTrace();
            }
        } else {
            Log.v("FCM_BASIC_ERROR", responseString);
        }
    }

    //this method gets events history for the selected child in the dropdown
    private void getChildEventHistory(String parentId, String fcm_token, String childId) {
        new NetworkPostRequest(HomeActivity.this, Endpoints.GET_CHILD_EVENTS_HISTORY_URL, this::displayChildGeofenceEventsHistory, Endpoints.GET_CHILD_EVENTS_HISTORY_TASK).execute(parentId, childId);
    }

    private void displayChildGeofenceEventsHistory(Context context, Integer status, String responseString) {
        Log.v("FCM_EVENTS_HISTORY", responseString);
        if (!responseString.equals("fail")) {
            try {
                JSONArray jsonArray = new JSONArray(responseString);
                if (jsonArray.length() == 0) {
                    recyclerView.setVisibility(View.GONE);
                    textViewMessage.setText(R.string.no_geofence_event_message);
                }
                else {
                    recyclerView.setVisibility(View.VISIBLE);
                    layoutManager = new LinearLayoutManager(HomeActivity.this);
                    recyclerViewAdapter = new RecyclerViewAdapter(parseEventsToList(jsonArray));
                    recyclerView.setLayoutManager(layoutManager);
                    recyclerView.setAdapter(recyclerViewAdapter);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    //This method parses the geofence events JSON into an arraylist of Geofence objects
    //The arraylist is sent to the recyclerview adapter
    private ArrayList<GeofenceEvent> parseEventsToList(JSONArray jsonArray) throws JSONException {
        ArrayList arrayList = new ArrayList<GeofenceEvent>();
        for (int i=0; i<jsonArray.length(); i++) {
            String geofenceId = jsonArray.getJSONObject(i).getString("geofenceId");
            String geofenceName = jsonArray.getJSONObject(i).getString("geofenceName");
            double latitude = jsonArray.getJSONObject(i).getDouble("latitude");
            double longitude = jsonArray.getJSONObject(i).getDouble("longtitude");
            double accuracy = jsonArray.getJSONObject(i).getDouble("accuracy");
            float speed = jsonArray.getJSONObject(i).getLong("speed");
            double altitude = jsonArray.getJSONObject(i).getDouble("altitude");
            float bearing = jsonArray.getJSONObject(i).getLong("bearing");
            String timestamp = jsonArray.getJSONObject(i).getString("timestamp");
            Log.v("FCM_ARRAY", geofenceName);
            arrayList.add(new GeofenceEvent(geofenceId, geofenceName, latitude, longitude, accuracy, speed, altitude, bearing, timestamp));
        }

        return arrayList;
    }
}
