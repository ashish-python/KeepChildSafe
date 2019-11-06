package com.parentapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.parentapp.stores.TokenStore;

import org.json.JSONArray;

public class BaseAppCompatActivity extends AppCompatActivity {
    public static final int FINISH_NO_ACTIVITY = 0;
    public static final int FINISH_CURRENT_ACTIVITY = 1;
    public static final int FINISH_ALL_ACTIVITIES = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base_app_compat);
    }

    public void startActivity(Context fromCls, Class<?> toCls, int finish) {
        Intent intent = new Intent(fromCls, toCls);
        startActivity(intent);
        switch (finish) {
            case FINISH_NO_ACTIVITY:
                break;
            case FINISH_CURRENT_ACTIVITY:
                finish();
                break;
            case FINISH_ALL_ACTIVITIES:
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                break;
        }
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

}
