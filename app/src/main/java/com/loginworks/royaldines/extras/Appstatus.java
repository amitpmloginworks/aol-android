package com.loginworks.royaldines.extras;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class Appstatus {

    private static Appstatus instance = new Appstatus();
    private static Context context;
    private ConnectivityManager connectivityManager;
    private NetworkInfo wifiInfo, mobileInfo;
    private boolean connected = false;

    public static Appstatus getInstance(Context ctx) {
        context = ctx;
        return instance;
    }

    public boolean isOnline() {
        try {
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
//            Handler handler = new Handler(Looper.getMainLooper());
//            handler.post(new Runnable() {
//                @Override
//                public void run() {
//            ((Activity)context).runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                    try {
//                        if (connected) {
//
//                            URL url = new URL("http://www.google.com");
//                            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                            urlConnection.connect();
//                            int respCode = urlConnection.getResponseCode();
//
//                            if (respCode != 200) {
//                                connected = false;
//                            }
//                            urlConnection.disconnect();
//                        }
//                    } catch (MalformedURLException ex) {
//                        Log.e("APP STATUS 1", Log.getStackTraceString(ex));
//                        connected = false;
//                    } catch (IOException ex) {
//                        Log.e("APP STATUS 2", Log.getStackTraceString(ex));
//                        connected = false;
//                    }
//                }
//            });
            return connected;


        } catch (Exception e) {
            e.printStackTrace();
            Log.e("CONNECTIVITY ::", e.toString());
        }
        return connected;
    }
}
