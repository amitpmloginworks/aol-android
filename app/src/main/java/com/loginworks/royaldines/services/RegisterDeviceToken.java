package com.loginworks.royaldines.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.loginworks.royaldines.R;
import com.loginworks.royaldines.extras.Appstatus;
import com.loginworks.royaldines.extras.Const;
import com.loginworks.royaldines.extras.JsonRequestAll;
import com.loginworks.royaldines.extras.MyPreferences;
import com.loginworks.royaldines.extras.ProgressDialogUtils;
import com.loginworks.royaldines.extras.ServiceURL;

import org.json.JSONObject;

/**
 * Created by ujjwal on 4/26/2017.
 */

public class RegisterDeviceToken extends Service {

    private static RegisterDeviceToken registerDeviceToken = null;
    private MyPreferences myPreferences;
    private Context mContext;

    public static boolean isServiceRunning() {
        if (registerDeviceToken == null)
            return false;
        else return true;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerDeviceToken = new RegisterDeviceToken();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        myPreferences = MyPreferences.getInstance();
        mContext = this;

        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myPreferences.setIMEI(mContext, telephonyManager.getDeviceId() + getPackageName());

        Log.e("IMEI SERVICE", ":::  " + myPreferences.getIMEI(this));

        if (myPreferences.getIMEI(mContext).length() > 0 && myPreferences.getDeviceToken(mContext).length() > 0) {
            Log.e(getClass().getSimpleName(), "IMEI:::   " + myPreferences.getIMEI(mContext));
            Log.e(getClass().getSimpleName(), "TOKEN:::   " + myPreferences.getDeviceToken(mContext));
            hitbackgroundTask(mContext,
                    myPreferences.getIMEI(mContext),
                    myPreferences.getDeviceToken(mContext));
        }

        return START_STICKY;

    }

    /*Authenticating user, based on there credentails*/
    private void hitbackgroundTask(final Context mActivity, String imei, String regId) {

        try {
            if (Appstatus.getInstance(mActivity).isOnline()) {

                //JSONObject setRequestHome = JsonRequestAll.setRequestHome(branchId);
                JSONObject setRequestHome = JsonRequestAll.createFCMRegisterRequest(imei, regId);
                Log.e("TOKEN REQUEST:::", setRequestHome.toString());

                WebServiceAbstract webServiceAbstract = new WebServiceAbstract() {
                    @Override
                    public void onComplete(JSONObject jsonObject) {
                        try {
                            if (jsonObject != null) {
                                ProgressDialogUtils.stopProgress();
                                afterResponse(jsonObject);
                                Log.e("TOKEN RESPONSE:::", jsonObject.toString());
                            } else {
                                ProgressDialogUtils.stopProgress();
                                //Toast.makeText(activity, getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };

                WebServiceClient.callPOSTService(ServiceURL.FCM_REGISTER, setRequestHome,
                        Const.JSON_OBJECT_RESPONSE, myPreferences.getAolToken(mActivity), webServiceAbstract, Request.Method.POST);
            } else {
                Toast.makeText(mActivity, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                ProgressDialogUtils.stopProgress();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void afterResponse(JSONObject jsonObject) {
        Log.e("FCM RESPONSE ",""+jsonObject.toString());
        stopSelf();
    }
}
