package com.parentapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.parentapp.constants.Constants;
import com.parentapp.listeners.BaseListener;
import com.parentapp.listeners.OnSwipeTouchListener;
import com.parentapp.stores.PermissionStore;
import com.parentapp.stores.TokenStore;
import com.parentapp.utils.NetworkPostRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class HomeActivity extends BaseAppCompatActivity {

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

    private String firstName;
    private String lastName;
    private JSONArray geofencesArray;

    private static int LOCATION_PERMISSION_CODE = 123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        initViews();
        setupClickListeners();
        setupSwipeListener();
        setHomeTranslationDistance();
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean permission = checkLocationPermission();
        if (permission) {
            showSuccessMessage();
            setFCMToken();
        }
        closeSettings();
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
    }

    private void setupClickListeners() {
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isHomeFullyVisible) {
                    openSettings();
                } else {
                    closeSettings();
                }
            }
        });

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeSettings();
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
                closeSettings();
            }
        });
    }

    private void setFCMToken() {
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM_TOKEN: ", "getInstanceId failed", task.getException());
                            return;

                        }
                        // Get Instance ID token
                        String token = task.getResult().getToken();

                        // Log and toast
                        TokenStore tokenStore = TokenStore.getInstance(getApplicationContext());
                        if (!token.equals(tokenStore.getFCMToken())) {
                            tokenStore.setFCMToken(token);
                        }
                        Log.v("FCM_TOKEN: ", token);
                    }
                });
    }

    private void setHomeTranslationDistance() {
        Display display = getWindowManager().getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        homeTranslationDistance = (int) -(displaySize.x * 0.65);
    }

    private void openSettings() {
        topLayout.setVisibility(View.VISIBLE);
        homeLayout.animate().x(homeTranslationDistance).start();
        isHomeFullyVisible = false;
    }

    private void closeSettings() {
        homeLayout.animate().x(0).y(0).z(0).start();
        isHomeFullyVisible = true;
        topLayout.setVisibility(View.GONE);
    }

    //Check if the user has allowed location tracking
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
        textViewMessage.setText(R.string.welcome_message);
        locationPermissionBtn.setVisibility(View.GONE);
    }
}
