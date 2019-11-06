package com.parentapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.parentapp.constants.Constants;
import com.parentapp.listeners.BaseListener;
import com.parentapp.stores.TokenStore;
import com.parentapp.utils.NetworkPostRequest;

public class SignInActivity extends BaseAppCompatActivity implements BaseListener {
    private EditText emailET;
    private EditText passwordET;
    private Button signInBtn;
    private TextView errorMessageTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        initViews();
        setupListeners();
    }

    private void initViews() {
        emailET = findViewById(R.id.email);
        passwordET = findViewById(R.id.password);
        signInBtn = findViewById(R.id.sign_in_btn);
        errorMessageTV = findViewById(R.id.error_message_tv);
    }

    private void setupListeners() {
        signInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismissKeyboard();
                signInBtn.setEnabled(false);
                signIn(emailET.getText().toString(), passwordET.getText().toString());
            }
        });
    }

    private void signIn(String email, String password) {
        new NetworkPostRequest(this, Constants.SIGN_IN_URL, this::callback, Constants.SIGN_IN).execute(email, password);
    }

    @Override
    public void callback(Context context, Integer status, String responseString) {
        if (responseString.equals("fail")) {
            signInBtn.setEnabled(true);
            errorMessageTV.setVisibility(View.VISIBLE);
            errorMessageTV.setText(R.string.login_error);
        } else {
            TokenStore.getInstance(getApplicationContext()).setUser(responseString);
            startActivity(SignInActivity.this, MainActivity.class, FINISH_CURRENT_ACTIVITY);
        }
        Log.v("FCM_RETURNEDD", responseString);
    }

    public void dismissKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = this.getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        if (imm.isAcceptingText()) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
