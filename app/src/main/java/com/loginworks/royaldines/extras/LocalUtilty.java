package com.loginworks.royaldines.extras;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import static android.content.Context.TELEPHONY_SERVICE;


public class LocalUtilty {

    public static String[] PERMISSIONS = {
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_PHONE_STATE
//            Manifest.permission.RECEIVE_SMS
    };
    private static LocalUtilty localUtility = null;

    public static LocalUtilty getInstance() {
        if (localUtility == null) {
            localUtility = new LocalUtilty();
        }
        return localUtility;
    }

    public static boolean hasPermissions(Context context, String[] permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {


                    return false;
                }
            }


        }
        return true;
    }

    public static void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/Lato_bold.ttf"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
    * Getting device ID
    * */
    public String getDeviceID(Context context) {
        String deviceId_str = null;
        TelephonyManager telephonyManager = null;
        telephonyManager = (TelephonyManager) context.getSystemService(TELEPHONY_SERVICE);
        deviceId_str = telephonyManager.getDeviceId();
        return deviceId_str;
    }

    /* Getting the system OS version */
    public String getOfflineDate() {
        Date date = new Date();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        // Use Parsia's time zone to format the date in
        df.setTimeZone(TimeZone.getTimeZone("GMT-6:00"));  //(GMT-6:00) France
        String franceTime = df.format(date);
        //Log.d(" PARSIA TIME ::", " " + parsiaTime);
        return franceTime;
    }


//    public void copyDatabase(Activity mActivity){
//        try {
//            File sd = Environment.getExternalStorageDirectory();
//            File data = Environment.getDataDirectory();
//
//            if (sd.canWrite()) {
//                String currentDBPath = "/data/data/" + mActivity.getPackageName() + "/databases/AOL.db";
//                String backupDBPath = "AOL-backup.db";
//                File currentDB = new File(currentDBPath);
//                File backupDB = new File(sd, backupDBPath);
//
//                if (currentDB.exists()) {
//                    FileChannel src = new FileInputStream(currentDB).getChannel();
//                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
//                    dst.transferFrom(src, 0, src.size());
//                    src.close();
//                    dst.close();
//                }
//            }
//        } catch (Exception e) {
//           e.printStackTrace();
//            Log.d("COPY DATABASE ", " YES ");
//        }
//    }


}
