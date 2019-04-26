package com.loginworks.royaldines.extras;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;


public class AppOnLeaseApplication extends MultiDexApplication {

    private static AppOnLeaseApplication mApplication;
    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        mApplication = this;
        MultiDex.install(this);

    }

    public static void setErrorDetailEnabled(boolean isEnabled) {
    }


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }


    public static AppOnLeaseApplication getApplication() {
        return mApplication;
    }

    public static Context getApplicationCtx() {
        return mApplication.getApplicationContext();
    }

}

