package com.parentapp.services;

import android.app.Notification;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.parentapp.activities.NotificationHelper;
import com.parentapp.activities.R;

public class MessagingService extends FirebaseMessagingService {
    private NotificationManagerCompat notificationManager;
    private static final int NOTIFICATION_ID = 42;
    public static final String FCM_TOKEN = "fcm_token";
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getNotification().getTitle(), remoteMessage.getNotification().getBody());
    }

    private void showNotification(String title, String body){
        Notification notification = new NotificationCompat.Builder(this, NotificationHelper.CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(body)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
                .build();

        notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(42, notification);
    }

    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("GEO_TRIGGER_TOKEN", token);
        //send the token in Shared Preferences



    }


}

