package com.loginworks.royaldines.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.loginworks.royaldines.R;
import com.loginworks.royaldines.databasehandler.DataHandlerDB;
import com.loginworks.royaldines.extras.Appstatus;
import com.loginworks.royaldines.extras.Const;
import com.loginworks.royaldines.extras.JsonRequestAll;
import com.loginworks.royaldines.extras.LocalUtilty;
import com.loginworks.royaldines.extras.MyPreferences;
import com.loginworks.royaldines.extras.ServiceURL;
import com.loginworks.royaldines.models.BranchParser;
import com.loginworks.royaldines.services.LocationService;
import com.loginworks.royaldines.services.RegisterDeviceToken;
import com.loginworks.royaldines.services.WebServiceAbstract;
import com.loginworks.royaldines.services.WebServiceClient;

import org.json.JSONObject;

import java.util.ArrayList;

public class Splash extends AppCompatActivity {

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final int PERMISSION_REQUEST = 1212;
    private Activity mActivity;
    private MyPreferences myPreferences;
    private DataHandlerDB dataHandlerDB;
    private Intent intent;
    private AlertDialog alert;
    private boolean isMultiBranchRunning = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Log.e("SPLASH SCREEN", "TIME CHECK");

        mActivity = this;
        myPreferences = MyPreferences.getInstance();
        dataHandlerDB = DataHandlerDB.getInstance();
        Log.e("Timer Running", "Timer Running");

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LocalUtilty.hasPermissions(mActivity, LocalUtilty.PERMISSIONS)) {
            if (isGPSEnable()) {
                if (!LocationService.isServiceRunning()) {
                    startService(new Intent(mActivity, LocationService.class));
                }
                    startService(new Intent(mActivity, RegisterDeviceToken.class));
                nextStep();
            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        settingsrequest();
                    }
                });
            }
        } else {
            ActivityCompat.requestPermissions(mActivity, LocalUtilty.PERMISSIONS, PERMISSION_REQUEST);
        }

    }



    private boolean isGPSEnable() {
        LocationManager manager = (LocationManager) mActivity.getSystemService(Context.LOCATION_SERVICE);
        boolean statusOfGPS = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return statusOfGPS;
    }

    public void settingsrequest() {

        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                .addApi(LocationServices.API)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(30 * 1000);
        locationRequest.setFastestInterval(5 * 1000);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true); //this is the key ingredient

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                final LocationSettingsStates state = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.

                        Log.e("Location Success", "Location Success");
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        Log.e("Resolution Required", "Resolution Required");
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(mActivity, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.
                        Log.e("Setting Unchanged", "Setting Unchanged");
                        break;
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e("Location onResult", "Location OnActiovityResult");
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.e("Location onResultCancel", "Location OnActiovityResult Cancel");
                        break;
                }
                break;
        }
    }

    //Verify user's response of the permission requested
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST: {
                if (grantResults.length > 0 && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    //nextStep();
                } else {
                    Log.e("onRequestPerm", "::else ");

                }
                break;
            }
        }
    }

    private void nextStep() {
        int branchCount = dataHandlerDB.getBranchCount();
        Log.e("BRANCH STORED:: ", "::: Count::  " + branchCount);
        switch (branchCount) {
            case 0:
                if (Appstatus.getInstance(mActivity).isOnline()) {
                    hitLocationTask();
                } else {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(mActivity, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
                break;
            case 1:
                ArrayList<BranchParser> listBranches = dataHandlerDB.getBranchData();
                if (listBranches != null) {
                    myPreferences.setBranchId(mActivity, listBranches.get(0).getBranch_id().trim());
                    myPreferences.setBranchName(mActivity, listBranches.get(0).getLocation_name().trim());
                    myPreferences.setBranchAddress(mActivity, listBranches.get(0).getAddress().trim());
                    myPreferences.setBranchCity(mActivity, listBranches.get(0).getCity().trim());
                    myPreferences.setBranchState(mActivity, listBranches.get(0).getState().trim());
                    myPreferences.setBranchCountry(mActivity, listBranches.get(0).getCountry().trim());
                    myPreferences.setBranchZipcode(mActivity, listBranches.get(0).getZipcode().trim());
                    myPreferences.setBranchMobile(mActivity, listBranches.get(0).getMobile().trim());
                    myPreferences.setBranchPhone(mActivity, listBranches.get(0).getPhone().trim());
                    myPreferences.setBranchLatitude(mActivity, listBranches.get(0).getLatitude().trim());
                    myPreferences.setBranchLongitude(mActivity, listBranches.get(0).getLongitude().trim());
                    myPreferences.setBranchEmail(mActivity, listBranches.get(0).getEmail().trim());
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            intent = new Intent(mActivity, HomePage.class);
                            startActivity(intent);
                            finish();
                        }
                    }, 2000);

                }
                break;
            default:
                intent = new Intent(mActivity, BranchSelection.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    private void hitLocationTask() {
        try {
            if (!isMultiBranchRunning) {
                isMultiBranchRunning = true;

                JSONObject setRequestMultibranch = JsonRequestAll.setRequestMultibranch();
                Log.e("Multibranch REQUEST:::", "" + setRequestMultibranch);
                WebServiceAbstract webServiceAbstract = new WebServiceAbstract() {
                    @Override
                    public void onComplete(JSONObject jsonObject) {
                        if (jsonObject != null) {
                            Log.e("Multibranch Response:::", jsonObject.toString());
                            new BranchParser(jsonObject);
                            isMultiBranchRunning = false;
                            onResume();
                        }
                    }
                };
                WebServiceClient.callPOSTService(ServiceURL.BRANCH, setRequestMultibranch,
                        Const.JSON_OBJECT_RESPONSE, null, webServiceAbstract, Request.Method.POST);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            isMultiBranchRunning = false;
        }

    }
}
