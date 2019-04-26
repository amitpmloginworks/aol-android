package com.loginworks.royaldines.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.loginworks.royaldines.R;
import com.loginworks.royaldines.activity.HomePage;

import org.json.JSONObject;

/**
 * Created by ujjwal on 4/26/2017.
 */

public class MYFirebaseMessagingService extends FirebaseMessagingService {

    private String TAG = getClass().getSimpleName();

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());

        // Check if message contains a notification payload.
        if (remoteMessage.getData() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getData().toString());
            handleNotification(remoteMessage.getData().get("body"), getString(R.string.noti_title));
        }
    }

    private void handleNotification(String body, String title) {

        Log.e("NOTIFICATION BODY", ":::  " + body);

        try {

            JSONObject object = new JSONObject(body);

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            mBuilder.setSmallIcon(getSmallIcon());
            mBuilder.setLargeIcon(((BitmapDrawable) getResources().getDrawable(R.drawable.notif_large_icon)).getBitmap());
            mBuilder.setColor(ContextCompat.getColor(this, R.color.colorPrimary));
            mBuilder.setContentTitle(title);
            mBuilder.setAutoCancel(true);
            mBuilder.setStyle(new android.support.v4.app.NotificationCompat.BigTextStyle().bigText(object.getString("m")));

            Intent resultIntent = new Intent(this, HomePage.class);
            resultIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            int requestID = (int) System.currentTimeMillis();

            PendingIntent resultPendingIntent = PendingIntent.getActivity(this, requestID, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(resultPendingIntent);
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            int notificationID = (int) System.currentTimeMillis();
            mNotificationManager.notify(notificationID, mBuilder.build());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private int getSmallIcon() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return R.drawable.logo_trans1;
        } else {
            return R.drawable.launcher_logo;
        }
    }
}
