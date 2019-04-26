package com.loginworks.royaldines.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.loginworks.royaldines.R;
import com.loginworks.royaldines.extras.Appstatus;
import com.loginworks.royaldines.extras.Const;
import com.loginworks.royaldines.extras.JsonRequestAll;
import com.loginworks.royaldines.extras.MyPreferences;
import com.loginworks.royaldines.extras.ProgressDialogUtils;
import com.loginworks.royaldines.extras.ServiceURL;
import com.loginworks.royaldines.models.AboutUs;
import com.loginworks.royaldines.services.WebServiceAbstract;
import com.loginworks.royaldines.services.WebServiceClient;

import org.json.JSONObject;

public class AboutUS extends AppCompatActivity {

    private Activity mContext;
    private String app_id, branch_id;
    private String TAG = getClass().getSimpleName();
    private TextView mAboutUs;
    private MyPreferences myPreferences;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        myPreferences = MyPreferences.getInstance();
        mContext = this;
        findViews();
        //Hit the service for About Us details
        hitAboutUs(mContext, Const.APP_ID, myPreferences.getBranchId(mContext));
    }

    private void findViews() {

        mAboutUs = (TextView) findViewById(R.id.txt_about_us);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_about_us);
        setSupportActionBar(mToolbar);
        mAboutUs.setMovementMethod(new ScrollingMovementMethod());

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void hitAboutUs(final Activity mContext, String app_id, String branch_id) {

        try {
            if (Appstatus.getInstance(mContext).isOnline()) {
                ProgressDialogUtils.startProgressBar(mContext, "Please Wait...");
                JSONObject setRequestAboutUs = JsonRequestAll.createJsonParamsAboutUs(app_id, branch_id);
                Log.e(TAG + " REQUEST ::+", setRequestAboutUs + "");
                WebServiceAbstract webServiceAbstract = new WebServiceAbstract() {
                    @Override
                    public void onComplete(JSONObject jsonObject) {
                        try {
                            if (jsonObject != null) {
                                Log.e(TAG, jsonObject.toString());
                                ProgressDialogUtils.stopProgress();
                                afterResponse(mContext, jsonObject);
                            }else{
                                ProgressDialogUtils.stopProgress();
                                Toast.makeText(mContext, getResources().getString(R.string.tryagain), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };
                WebServiceClient.callPOSTService(ServiceURL.ABOUT_US,
                        setRequestAboutUs, Const.JSON_OBJECT_RESPONSE, myPreferences.getAolToken(mContext), webServiceAbstract, Request.Method.POST);

            } else {
                Toast.makeText(mContext, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                ProgressDialogUtils.stopProgress();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void afterResponse(Context mContext, JSONObject jsonObject) {
        try {
            Log.e(TAG, " RESPONSE :: " + jsonObject);
            AboutUs aboutUS = new AboutUs(jsonObject);
            if (aboutUS.getStatus().equals("1")) {
                handleSuccess(aboutUS.getDescription());
            } else {
                handleFailure(aboutUS.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleFailure(String message) {
        Log.d(TAG, "ABOUT US MESSAGE :: " + message);
    }

    private void handleSuccess(String message) {
        Log.d(TAG, " ABOUT US MESSAGE :: " + message);
        mAboutUs.setText(message);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
