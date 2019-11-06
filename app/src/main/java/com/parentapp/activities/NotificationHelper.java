package com.parentapp.activities;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

public class NotificationHelper extends Application {
    public static final String CHANNEL_ID = "geofence_notification_id";
    public static final String CHANNEL_NAME = "geofence_notification_channel";

    @Override
    public void onCreate() {
        super.onCreate();
        createNotificationChannels();
    }

    private void createNotificationChannels(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            //********* Create notification channel ************//
            NotificationChannel notificationChannel =new NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH
            );
            notificationChannel.setDescription(this.getString(R.string.notification_channel_description));

            //********** Add notification channel to Notification Manager *********//
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(notificationChannel);
        }
    }
}
