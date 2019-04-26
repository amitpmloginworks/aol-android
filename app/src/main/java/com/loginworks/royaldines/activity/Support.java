package com.loginworks.royaldines.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.loginworks.royaldines.services.WebServiceAbstract;
import com.loginworks.royaldines.services.WebServiceClient;

import org.json.JSONObject;

import java.util.regex.Pattern;

public class Support extends AppCompatActivity implements View.OnClickListener {


    private EditText et_fname, et_lname, et_email, et_mobile, et_query;
    private Button submit_btn;
    private Toolbar mToolbar;
    private TextView mSupport;
    private Activity mActivity;
    private Pattern alpha_numeric_pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-zA-Z])(?=\\S+$).{6,}$");
    private Pattern character_Pattern = Pattern.compile("[a-zA-Z ]+");
    private Pattern phonePattern = Pattern.compile("[0-9]+");
    private String TAG = getClass().getName();
    private MyPreferences myPreferences;

    private static boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support);
        mActivity = this;
        myPreferences = MyPreferences.getInstance();
        findViews();
    }

    /**
     * Find the Views in the layout
     */
    private void findViews() {

        et_fname = (EditText) findViewById(R.id.et_fname);
        et_lname = (EditText) findViewById(R.id.et_lname);
        et_email = (EditText) findViewById(R.id.et_email);
        et_mobile = (EditText) findViewById(R.id.et_mobile);
        et_query = (EditText) findViewById(R.id.et_query);

        mSupport = (TextView) findViewById(R.id.txt_header_title_support);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_support);

        submit_btn = (Button) findViewById(R.id.button2);

        setSupportActionBar(mToolbar);

        //fragmentManager = getSupportFragmentManager();

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        submit_btn.setOnClickListener(this);
    }

    private void hitSupportService(final Activity mActivity, String branchId, String st_fname, String st_lname,
                                   String st_email, String st_mobile, String st_query) {

        try {
            if (Appstatus.getInstance(mActivity).isOnline()) {
                ProgressDialogUtils.startProgressBar(mActivity, "Please Wait...");

                JSONObject setRequestSupport = JsonRequestAll.createJsonParamsSupport(branchId, st_fname, st_lname, st_email,
                        st_mobile, st_query);
                Log.e(TAG + " REQUEST ::", setRequestSupport + "");

                WebServiceAbstract webServiceAbstract = new WebServiceAbstract() {
                    @Override
                    public void onComplete(JSONObject jsonObject) {

                        try {
                            if (jsonObject != null) {
                                Log.e(TAG, jsonObject.toString());
                                ProgressDialogUtils.stopProgress();
                                afterResponse(mActivity, jsonObject);
                            } else {
                                ProgressDialogUtils.stopProgress();
                                Toast.makeText(mActivity, getResources().getString(R.string.tryagain), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };
                WebServiceClient.callPOSTService(ServiceURL.SUPPORT, setRequestSupport,
                        Const.JSON_OBJECT_RESPONSE, myPreferences.getAolToken(mActivity), webServiceAbstract, Request.Method.POST);
            } else {
                Toast.makeText(mActivity, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                ProgressDialogUtils.stopProgress();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void afterResponse(Context context, JSONObject jsonObject) {
        try {
            if (jsonObject != null) {
                int status = jsonObject.getInt("status");
                String message = jsonObject.getString("message");
                if (status == 1) {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Handle button click events
     */
    @Override
    public void onClick(View v) {
        if (v == submit_btn) {
            String st_fname = et_fname.getText().toString().trim();
            String st_lname = et_lname.getText().toString().trim();
            String st_email = et_email.getText().toString().trim();
            String st_mobile = et_mobile.getText().toString().trim();
            String st_query = et_query.getText().toString().trim();

            if (validateInFields(st_fname, st_lname, st_email, st_mobile, st_query)) {
                String branchId = MyPreferences.getInstance().getBranchId(mActivity);
                hitSupportService(mActivity, branchId, st_fname, st_lname, st_email, st_mobile, st_query);
            }
        }
    }

    private boolean validateInFields(String st_fname, String st_lname, String st_email, String st_mobile,
                                     String st_query) {
        Boolean validation = true;


        try {
            int digit_mobile = Integer.parseInt(st_mobile);
            digit_mobile = digit_mobile + 1;
            if (digit_mobile == 1) {
                Toast.makeText(mActivity, getResources().getString(R.string.mobile_cannot_be_zero), Toast.LENGTH_SHORT).show();
                et_mobile.requestFocus();
                validation = false;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        if (st_fname.equalsIgnoreCase("")) {
            Toast.makeText(mActivity, getResources().getString(R.string.empty_fname), Toast.LENGTH_SHORT).show();
            et_fname.requestFocus();
            validation = false;

        } else if (!(character_Pattern.matcher(st_fname).matches())) {
            Toast.makeText(mActivity, getResources().getString(R.string.fname_valid), Toast.LENGTH_SHORT).show();
            et_fname.requestFocus();
            validation = false;

        } else if (st_lname.equalsIgnoreCase("")) {
            Toast.makeText(mActivity, getResources().getString(R.string.empty_lname), Toast.LENGTH_SHORT).show();
            et_lname.requestFocus();
            validation = false;

        } else if (!(character_Pattern.matcher(st_lname).matches())) {
            Toast.makeText(mActivity, getResources().getString(R.string.lname_valid), Toast.LENGTH_SHORT).show();
            et_lname.requestFocus();
            validation = false;

        } else if (st_email.equalsIgnoreCase("")) {
            Toast.makeText(mActivity, getResources().getString(R.string.enter_email), Toast.LENGTH_SHORT).show();
            et_email.requestFocus();
            validation = false;

        } else if (!isValidEmail(st_email)) {
            Toast.makeText(mActivity, getResources().getString(R.string.valid_email), Toast.LENGTH_SHORT).show();
            et_email.requestFocus();
            validation = false;

        } else if (st_mobile.equalsIgnoreCase("")) {
            Toast.makeText(mActivity, getResources().getString(R.string.empty_mobile), Toast.LENGTH_SHORT).show();
            et_mobile.requestFocus();
            validation = false;

        } else if (!(phonePattern.matcher(st_mobile).matches())) {
            Toast.makeText(mActivity, getResources().getString(R.string.valid_mobile), Toast.LENGTH_SHORT).show();
            et_mobile.requestFocus();
            validation = false;

        } else if ((st_mobile.length() < 10) || (st_mobile.length() > 10)) {
            Toast.makeText(mActivity, getResources().getString(R.string.valid_mobile), Toast.LENGTH_SHORT).show();
            et_mobile.requestFocus();
            validation = false;

        } else if (st_query.equalsIgnoreCase("")) {
            Toast.makeText(mActivity, getResources().getString(R.string.fill_query), Toast.LENGTH_SHORT).show();
            et_query.requestFocus();
            validation = false;
        }



        return validation;
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
