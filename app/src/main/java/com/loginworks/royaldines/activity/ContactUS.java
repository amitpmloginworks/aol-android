package com.loginworks.royaldines.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import com.loginworks.royaldines.R;
import com.loginworks.royaldines.extras.Appstatus;
import com.loginworks.royaldines.extras.Const;
import com.loginworks.royaldines.extras.JsonRequestAll;
import com.loginworks.royaldines.extras.MyPreferences;
import com.loginworks.royaldines.extras.ProgressDialogUtils;
import com.loginworks.royaldines.extras.ServiceURL;
import com.loginworks.royaldines.models.ContactAddress;
import com.loginworks.royaldines.services.WebServiceAbstract;
import com.loginworks.royaldines.services.WebServiceClient;

import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ContactUS extends AppCompatActivity implements View.OnClickListener {

    SupportMapFragment mapFragment;
    private MyPreferences myPreferences;
    private Context mContext;
    private String TAG = getClass().getName();
    private GoogleMap mMap;
    private TextView tvAddress, tvMobile, tvEmail;
    private ContactAddress contactAddress;
    private Toolbar mToolbar;
    private TextView mAboutUs;
    private ImageView mLocation, mRestIcon, imv_contact_us;
    private LinearLayout mLinearL_Map, mRestLocationImage, mRestImage;
    private LatLng branchLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        findViews();
        mContext = this;
        myPreferences = MyPreferences.getInstance();
        hitContactUs((Activity) mContext, myPreferences.getBranchId(mContext));
    }


    private void findViews() {
        mAboutUs = (TextView) findViewById(R.id.txt_header_title_contact_us);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_contact_us);
        mLocation = (ImageView) findViewById(R.id.location_img);
        mLinearL_Map = (LinearLayout) findViewById(R.id.map_ll);
        mRestImage = (LinearLayout) findViewById(R.id.res_img_ll);
        mRestIcon = (ImageView) findViewById(R.id.rest_img_mip);
        imv_contact_us = (ImageView) findViewById(R.id.imv_contact_us);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        tvAddress = (TextView) findViewById(R.id.tv_address);
        tvEmail = (TextView) findViewById(R.id.tv_email);
        tvMobile = (TextView) findViewById(R.id.tv_mobile);

        tvMobile.setMovementMethod(LinkMovementMethod.getInstance());
        tvEmail.setMovementMethod(LinkMovementMethod.getInstance());

        mLocation.setOnClickListener(ContactUS.this);
        mRestIcon.setOnClickListener(ContactUS.this);
        tvEmail.setOnClickListener(this);
    }

    private void hitContactUs(final Activity mContext, String branchId) {

        try {
            if (Appstatus.getInstance(mContext).isOnline()) {
                ProgressDialogUtils.startProgressBar(mContext, "Please Wait...");

                JSONObject setRequestAboutUs = JsonRequestAll.createJsonParamsContactUs(branchId);
                Log.e(TAG + " REQUEST ::+", setRequestAboutUs + "");

                WebServiceAbstract webServiceAbstract = new WebServiceAbstract() {
                    @Override
                    public void onComplete(JSONObject jsonObject) {
                        try {
                            if (jsonObject != null) {
                                Log.e(TAG, jsonObject.toString());
                                ProgressDialogUtils.stopProgress();
                                afterResponse(mContext, jsonObject);
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };
                WebServiceClient.callPOSTService(ServiceURL.CONTACT_US,
                        setRequestAboutUs, Const.JSON_OBJECT_RESPONSE, myPreferences.getAolToken(mContext), webServiceAbstract, Request.Method.POST);

            } else {
                Toast.makeText(mContext, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                ProgressDialogUtils.stopProgress();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void afterResponse(Activity mContext, JSONObject jsonObject) {

        contactAddress = new ContactAddress(jsonObject);

        if (contactAddress.getStatus().equals("1")) {
            handleSuccess(contactAddress);
            branchLocation = getLocationFromAddress(mContext, contactAddress.getLocation_name());
        } else {
            handleFailure(contactAddress.getMessage());
        }
    }

    private void handleSuccess(final ContactAddress contactAddress) {
        tvAddress.setText(contactAddress.getAddress());
        tvMobile.setText(contactAddress.getPhone());
        tvEmail.setText(contactAddress.getEmail());


        Glide.with(mContext)
                .load(ServiceURL.BASE_IMAGE_PATH + contactAddress.getImg())
                .placeholder(R.mipmap.placeholder_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.placeholder_img)
                .into(imv_contact_us);

        imv_contact_us.setScaleType(ImageView.ScaleType.FIT_XY);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                try {
                    if (branchLocation != null) {
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(branchLocation));
                        mMap.addMarker(new MarkerOptions()
                                .position(branchLocation)
                                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                        mMap.animateCamera(CameraUpdateFactory.zoomTo(10.5f));
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        });

    }

    private void handleFailure(String message) {
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


    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.location_img:
                mLocation.setVisibility(View.GONE);
                mRestIcon.setVisibility(View.VISIBLE);
                mRestImage.setVisibility(View.GONE);
                mLinearL_Map.setVisibility(View.VISIBLE);
                break;

            case R.id.rest_img_mip:

                mLocation.setVisibility(View.VISIBLE);
                mRestIcon.setVisibility(View.GONE);
                mRestImage.setVisibility(View.VISIBLE);
                mLinearL_Map.setVisibility(View.GONE);

                break;

            case R.id.tv_email:
                try {
                    Intent emailIntent = new Intent(Intent.ACTION_SENDTO,
                            Uri.fromParts("mailto", tvEmail.getText().toString(), null));
                    emailIntent.putExtra(Intent.EXTRA_EMAIL, "");
                    emailIntent.putExtra(Intent.EXTRA_SUBJECT, "");
                    emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                    startActivity(Intent.createChooser(emailIntent, "Send Email..."));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;
        }

    }

    public LatLng getLocationFromAddress(Context context, String locationName) {
        if (!Geocoder.isPresent()) {
            Log.e(TAG, "Geocoder implementation not present !");
        }
        Geocoder geoCoder = new Geocoder(context, Locale.getDefault());

        try {
            int maxResult = 1;
            List<Address> addresses = geoCoder.getFromLocationName(locationName, maxResult);
            int tentatives = 0;
            while (addresses.size() == 0 && (tentatives < 10)) {
                addresses = geoCoder.getFromLocationName("<address goes here>", 1);
                tentatives++;
            }

            if (addresses.size() > 0) {

                Log.e(TAG, "reverse Geocoding : locationName " + locationName + "Latitude " + addresses.get(0).getLatitude());
                Log.e(TAG, addresses.size() + "");
                return new LatLng(addresses.get(0).getLatitude(), addresses.get(0).getLongitude());
            } else {
                //use http api
            }

        } catch (IOException e) {
            Log.d(TAG, "not possible finding LatLng for Address : " + locationName);
        }
        return null;
    }
}
