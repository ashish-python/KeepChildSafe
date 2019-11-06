package com.parentapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Point;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.parentapp.listeners.OnSwipeTouchListener;
import com.parentapp.stores.PermissionStore;
import com.parentapp.stores.TokenStore;

public class MainActivity extends BaseAppCompatActivity {
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
    private Button buttonLocationPermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (TokenStore.getInstance(getApplicationContext()).getUser().equals("")) {
            startActivity(MainActivity.this, SignInActivity.class, FINISH_NO_ACTIVITY);
        }
        else{
            startActivity(MainActivity.this, HomeActivity.class, FINISH_NO_ACTIVITY);
        }
    }
}
