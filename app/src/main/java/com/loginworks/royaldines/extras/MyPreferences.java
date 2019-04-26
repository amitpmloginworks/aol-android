package com.loginworks.royaldines.extras;

import android.content.Context;
import android.content.SharedPreferences;


public class MyPreferences {


    private static final String MyLogin_PREFERENCES = "AOL_Pref";
    private static MyPreferences myPreferences = null;

    private static String DEVICE_ID = "device_id";
    private static String AOL_TOKEN = "aol_token";
    private static String LATITUDE = "current_latitude";
    private static String LONGITUDE = "current_longitude";
    private static String BRANCH_NAME = "branch_name";
    private static String BRANCH_ID = "branch_id";
    private static String BRANCH_ADDRESS = "branch_address";
    private static String BRANCH_CITY = "branch_city";
    private static String BRANCH_STATE = "branch_state";
    private static String BRANCH_COUNTRY = "branch_country";
    private static String BRANCH_ZIPCODE = "branch_zipcode";
    private static String BRANCH_MOBILE = "branch_mobile";
    private static String BRANCH_PHONE = "branch_phone";
    private static String BRANCH_LATITUDE = "branch_latitude";
    private static String BRANCH_LONGITUDE = "branch_longitude";
    private static String BRANCH_EMAIL = "branch_email";
    private static String TAKE_AWAY = "take_away";
    private static String HOME_DELIVERY = "home_delivery";
    private static String IS_REORDER = "reorder";
    private static String IMEI = "imei";
    private static String DEVICE_TOKEN = "device_token";


    private static String USER_MOBILE = "user_mobile";
    private static String USER_NAME = "user_name";
    private static String LOGIN_SESSION = "login_session";
    private static String SERVICE_CHARGE = "service_charge";
    private static String PACKAGE_CHARGE = "package_charge";
    private static String TAX_STATUS = "tax_status";
    //
    private static String SGST_TAX_VALUE = "sgst_tax_value";
    private static String CGST_TAX_VALUE = "cgst_tax_value";
    //
    private static String OTP_SUPPORT = "otp_support";
    private static String MIN_ORDER = "min_order";
    private static String ADDRESS_EMAILID = "address_emailid";

    private static String DELIVERY_MOBILE = "delivery_mobile";

    public static MyPreferences getInstance() {
        if (myPreferences == null) {
            myPreferences = new MyPreferences();
        }
        return myPreferences;
    }

    private static SharedPreferences getPrefs(Context context) {
        return context.getSharedPreferences(MyLogin_PREFERENCES, Context.MODE_PRIVATE);
    }

    public String getUserMobile(Context context) {
        return getPrefs(context).getString(USER_MOBILE, "");
    }

    public void setUserMobile(Context context, String mobile) {
        getPrefs(context).edit().putString(USER_MOBILE, mobile).apply();
    }

    public String getUserName(Context context) {
        return getPrefs(context).getString(USER_NAME, "");
    }

    public void setUserName(Context context, String user) {
        getPrefs(context).edit().putString(USER_NAME, user).apply();
    }

    public boolean getSession(Context context) {
        return getPrefs(context).getBoolean(LOGIN_SESSION, false);
    }

    public void setSession(Context context) {
        getPrefs(context).edit().putBoolean(LOGIN_SESSION, true).apply();
    }

    public String getDeviceId(Context context) {
        return getPrefs(context).getString(DEVICE_ID, "");
    }

    public void setDeviceId(Context context, String deviceId) {
        // perform validation etc..
        getPrefs(context).edit().putString(DEVICE_ID, deviceId).apply();
    }

    public String getAolToken(Context context) {
        return getPrefs(context).getString(AOL_TOKEN, "");
    }

    public void setAolToken(Context context, String deviceId) {
        // perform validation etc..
        getPrefs(context).edit().putString(AOL_TOKEN, deviceId).apply();
    }

    public String getLatitude(Context context) {
        return getPrefs(context).getString(LATITUDE, "");
    }

    public void setLatitude(Context context, String latitude) {
        getPrefs(context).edit().putString(LATITUDE, latitude).apply();
    }

    public String getLongtitude(Context context) {
        return getPrefs(context).getString(LONGITUDE, "");
    }

    public void setLongtitude(Context context, String longtitude) {
        getPrefs(context).edit().putString(LONGITUDE, longtitude).apply();
    }

    public void setBranchName(Context context, String branchName) {
        getPrefs(context).edit().putString(BRANCH_NAME, branchName).apply();
    }

    public String getBranchName(Context context) {
        return getPrefs(context).getString(BRANCH_NAME, "");
    }

    public String getBranchId(Context context) {
        return getPrefs(context).getString(BRANCH_ID, "");
    }

    public void setBranchId(Context context, String branchName) {
        getPrefs(context).edit().putString(BRANCH_ID, branchName).apply();
    }

    public String getBranchAddress(Context context) {
        return getPrefs(context).getString(BRANCH_ADDRESS, "");
    }

    public void setBranchAddress(Context context, String branchName) {
        getPrefs(context).edit().putString(BRANCH_ADDRESS, branchName).apply();
    }

    public String getBranchCity(Context context) {
        return getPrefs(context).getString(BRANCH_CITY, "");
    }

    public void setBranchCity(Context context, String branchName) {
        getPrefs(context).edit().putString(BRANCH_CITY, branchName).apply();
    }

    public String getBranchState(Context context) {
        return getPrefs(context).getString(BRANCH_STATE, "");
    }

    public void setBranchState(Context context, String branchName) {
        getPrefs(context).edit().putString(BRANCH_STATE, branchName).apply();
    }

    public String getBranchCountry(Context context) {
        return getPrefs(context).getString(BRANCH_COUNTRY, "");
    }

    public void setBranchCountry(Context context, String branchName) {
        getPrefs(context).edit().putString(BRANCH_COUNTRY, branchName).apply();
    }

    public String getBranchZipcode(Context context) {
        return getPrefs(context).getString(BRANCH_ZIPCODE, "");
    }

    public void setBranchZipcode(Context context, String branchName) {
        getPrefs(context).edit().putString(BRANCH_ZIPCODE, branchName).apply();
    }

    public String getBranchMobile(Context context) {
        return getPrefs(context).getString(BRANCH_MOBILE, "");
    }

    public void setBranchMobile(Context context, String branchName) {
        getPrefs(context).edit().putString(BRANCH_MOBILE, branchName).apply();
    }

    public String getBranchPhone(Context context) {
        return getPrefs(context).getString(BRANCH_PHONE, "");
    }

    public void setBranchPhone(Context context, String branchName) {
        getPrefs(context).edit().putString(BRANCH_PHONE, branchName).apply();
    }

    public String getBranchLatitude(Context context) {
        return getPrefs(context).getString(BRANCH_LATITUDE, "");
    }

    public void setBranchLatitude(Context context, String branchName) {
        getPrefs(context).edit().putString(BRANCH_LATITUDE, branchName).apply();
    }

    public String getBranchLongitude(Context context) {
        return getPrefs(context).getString(BRANCH_LONGITUDE, "");
    }

    public void setBranchLongitude(Context context, String branchName) {
        getPrefs(context).edit().putString(BRANCH_LONGITUDE, branchName).apply();
    }

    public String getBranchEmail(Context context) {
        return getPrefs(context).getString(BRANCH_EMAIL, "");
    }

    public void setBranchEmail(Context context, String branchName) {
        getPrefs(context).edit().putString(BRANCH_EMAIL, branchName).apply();
    }


    public String getTakeAway(Context context) {
        return getPrefs(context).getString(TAKE_AWAY, "");
    }

    public void setTakeAway(Context context, String take_away) {
        getPrefs(context).edit().putString(TAKE_AWAY, take_away).apply();
    }

    public String getHomeDelivery(Context context) {
        return getPrefs(context).getString(HOME_DELIVERY, "");
    }

    public void setHomeDelivery(Context context, String home_delivery) {
        getPrefs(context).edit().putString(HOME_DELIVERY, home_delivery).apply();
    }

    public String getServiceCharge(Context context) {
        return getPrefs(context).getString(SERVICE_CHARGE, "");
    }

    public void setServiceCharge(Context context, String servicecharge) {
        getPrefs(context).edit().putString(SERVICE_CHARGE, servicecharge).apply();
    }

    public String getPackingCharge(Context context) {
        return getPrefs(context).getString(PACKAGE_CHARGE, "");
    }

    public void setPackingCharge(Context context, String packing_charge) {
        getPrefs(context).edit().putString(PACKAGE_CHARGE, packing_charge).apply();
    }

    public String getCGST_TaxValue(Context context) {
        return getPrefs(context).getString(CGST_TAX_VALUE, "");
    }

    public void setCGST_TaxValue(Context context, String packing_charge) {
        getPrefs(context).edit().putString(CGST_TAX_VALUE, packing_charge).apply();
    }

    public String getSGST_TaxValue(Context context) {
        return getPrefs(context).getString(SGST_TAX_VALUE, "");
    }

    public void setSGST_TaxValue(Context context, String packing_charge) {
        getPrefs(context).edit().putString(SGST_TAX_VALUE, packing_charge).apply();
    }

    public boolean isTaxStatus(Context context) {
        return getPrefs(context).getBoolean(TAX_STATUS, false);
    }

    public void setTaxStatus(Context context, boolean packing_charge) {
        getPrefs(context).edit().putBoolean(TAX_STATUS, packing_charge).apply();
    }

    public boolean isReorder(Context context) {
        return getPrefs(context).getBoolean(IS_REORDER, false);
    }

    public void setReorder(Context context, boolean bool) {
        getPrefs(context).edit().putBoolean(IS_REORDER, bool).apply();
    }

    public String getIMEI(Context context) {
        return getPrefs(context).getString(IMEI, "");
    }

    public void setIMEI(Context context, String packing_charge) {
        getPrefs(context).edit().putString(IMEI, packing_charge).apply();
    }

    public String getDeviceToken(Context context) {
        return getPrefs(context).getString(DEVICE_TOKEN, "");
    }

    public void setDeviceToken(Context context, String packing_charge) {
        getPrefs(context).edit().putString(DEVICE_TOKEN, packing_charge).apply();
    }

    public String getOtpSupport(Context context) {
        return getPrefs(context).getString(OTP_SUPPORT, "");
    }

    public void setOtpSupport(Context context, String otp_support) {
        getPrefs(context).edit().putString(OTP_SUPPORT, otp_support).apply();
    }

    public String getMinOrder(Context context) {
        return getPrefs(context).getString(MIN_ORDER, "");
    }

    public void setMinOrder(Context context, String min_order) {
        getPrefs(context).edit().putString(MIN_ORDER, min_order).apply();
    }

    public String getAddressEmailid(Context context) {
        return getPrefs(context).getString(ADDRESS_EMAILID, "");
    }

    public void setAddressEmailid(Context context, String address_emailid) {
        getPrefs(context).edit().putString(ADDRESS_EMAILID, address_emailid).apply();
    }

    public String getDELIVERY_MOBILE(Context context) {
        return getPrefs(context).getString(DELIVERY_MOBILE, "");
    }

    public void setDELIVERY_MOBILE(Context context, String delivery_mobile) {
        getPrefs(context).edit().putString(DELIVERY_MOBILE, delivery_mobile).apply();
    }
}





