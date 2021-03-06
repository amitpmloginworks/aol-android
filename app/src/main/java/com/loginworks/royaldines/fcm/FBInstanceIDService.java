package com.loginworks.royaldines.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.loginworks.royaldines.extras.MyPreferences;

import java.util.Timer;

/**
 * Created by Ashish on 4/26/2017.
 */

public class FBInstanceIDService extends FirebaseInstanceIdService {

    private MyPreferences myPreferences;
    private Timer timer;

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        myPreferences = MyPreferences.getInstance();

        String token = FirebaseInstanceId.getInstance().getToken();
        Log.e("FCM TOKEN", ":::  " + token + "   :::");
        if (token.length() > 0 && !myPreferences.getDeviceId(FBInstanceIDService.this).equals(token)) {
            myPreferences.setDeviceToken(FBInstanceIDService.this, token);
            Log.e("SAVED FCM TOKEN", ":::  " + myPreferences.getDeviceToken(FBInstanceIDService.this) + "   :::");

        } else {
            Log.e("FCM TOKEN", "SAME TOKEN FOUND");
        }

    }


}
