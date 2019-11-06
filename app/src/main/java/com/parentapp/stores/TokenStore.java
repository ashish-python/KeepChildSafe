package com.parentapp.stores;

import android.content.Context;

public class TokenStore extends BaseStore {
    private static String SHARED_PREFS = "TokenStore";
    private static TokenStore instance = null;
    private static final String FCM_TOKEN = "fcm";
    private static final String AUTH_TOKEN = "auth";
    private static final String USER = "user";
    private Context context;

    private TokenStore(Context context){
        super(SHARED_PREFS, context);
        this.context = context;
    }

    public static TokenStore getInstance(Context context){
        if(instance != null){
            return instance;
        }
        instance = new TokenStore(context);
        return instance;
    }

    public String getFCMToken() {
        return getString(FCM_TOKEN, null);
    }

    public String getAuthToken(){
        return getString(AUTH_TOKEN, null);
    }

    public void setFCMToken(String value){
        savePair(FCM_TOKEN, value);
    }

    public void setAuthToken(String value){
        savePair(AUTH_TOKEN, value);
    }

    public String getUser() {
        return getString(USER, "");
    }

    public void setUser(String value) {
        savePair(USER, value);
    }
}
