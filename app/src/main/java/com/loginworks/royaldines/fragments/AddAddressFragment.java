package com.loginworks.royaldines.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.loginworks.royaldines.R;
import com.loginworks.royaldines.activity.DashboardActivity;
import com.loginworks.royaldines.extras.Appstatus;
import com.loginworks.royaldines.extras.Const;
import com.loginworks.royaldines.extras.FragmentsName;
import com.loginworks.royaldines.extras.JsonRequestAll;
import com.loginworks.royaldines.extras.MyPreferences;
import com.loginworks.royaldines.extras.ProgressDialogUtils;
import com.loginworks.royaldines.extras.ServiceURL;
import com.loginworks.royaldines.models.CustomerAddress;
import com.loginworks.royaldines.services.WebServiceAbstract;
import com.loginworks.royaldines.services.WebServiceClient;
import com.loginworks.royaldines.utility.Util;

import org.json.JSONObject;

import java.util.regex.Pattern;

/**
 * Created by lw23 on 20-04-2017.
 */

public class AddAddressFragment extends Fragment implements View.OnClickListener {

    private EditText mCustomerName, edit_contry, mDeliveryMobile, mEmailID,
            mHouseNumber, mStreet, mCity, mLandmark,  mPincode /*mInstruction*/;
    private TextView mRegisteredMobile, mProceed;
    private Context mContext;
    private Util util;
    private MyPreferences myPreferences;
    private String customerName, registerMobile, deliveryMobile, emailId,
            houseNumber, street, city, pincode, saved_number;
    public static String st_landmark = null;
    private String TAG = getClass().getName();
    private TextView tvTitle;
    private SearchView searchView;
    private ImageView iv_location;
    private RelativeLayout ll_Cart;
    private ImageView imv_editOrder;
    private Pattern phonePattern = Pattern.compile("[0-9]+");

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_address, container, false);
        mContext = getActivity();
        myPreferences = MyPreferences.getInstance();
        util = Util.getInstance();

        findViews(view);

        if (myPreferences.getUserMobile(mContext) != null && myPreferences.getUserName(mContext) != null) {
            mRegisteredMobile.setText(myPreferences.getUserMobile(mContext));
            mCustomerName.setText(myPreferences.getUserName(mContext));
        }

        Bundle bundle = getArguments();
        if (bundle != null) {
            String value = bundle.getString("page");
            if (value.equalsIgnoreCase("deliveryInfo")) {
                mCustomerName.setEnabled(true);
            }
        }
        return view;
    }

    private void findViews(View view) {

        mCustomerName = (EditText) view.findViewById(R.id.delivery_username);
        mRegisteredMobile = (TextView) view.findViewById(R.id.delivery_registered_mobile_no);
        edit_contry = (EditText) view.findViewById(R.id.edit_contry);
        mDeliveryMobile = (EditText) view.findViewById(R.id.delivery_phone_no);
        mEmailID = (EditText) view.findViewById(R.id.delivery_email_id);
        mHouseNumber = (EditText) view.findViewById(R.id.delivery_house_no);
        mStreet = (EditText) view.findViewById(R.id.delivery_street_no);
        mCity = (EditText) view.findViewById(R.id.delivery_city);
        mLandmark = (EditText) view.findViewById(R.id.delivery_landmark);
//        mPincode = (EditText) view.findViewById(R.id.delivery_pincode);
//        mInstruction = (EditText) view.findViewById(R.id.delivery_instructions);
        edit_contry.setEnabled(false);
        edit_contry.setKeyListener(null);
        edit_contry.setFocusable(false);

        mProceed = (TextView) view.findViewById(R.id.delivery_proceed);
        mProceed.setOnClickListener(AddAddressFragment.this);

    }

    @Override
    public void onResume() {
        super.onResume();

        DashboardActivity.toolbar.setVisibility(View.VISIBLE);
        tvTitle = ((DashboardActivity) mContext).txt_Title;
        searchView = ((DashboardActivity) mContext).searchview;
        iv_location = ((DashboardActivity) mContext).imv_location;

        ll_Cart = ((DashboardActivity) mContext).ll_cart;
        imv_editOrder = ((DashboardActivity) mContext).imv_editOrder;
        ll_Cart.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.deliver_info));
        iv_location.setVisibility(View.GONE);
        searchView.setVisibility(View.GONE);
        imv_editOrder.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.delivery_proceed) {

            try {
                customerName = mCustomerName.getText().toString().trim();
                registerMobile = mRegisteredMobile.getText().toString().trim();
                deliveryMobile = mDeliveryMobile.getText().toString().trim();
                emailId = mEmailID.getText().toString().trim();
                houseNumber = mHouseNumber.getText().toString().trim();
                street = mStreet.getText().toString().trim();
                city = mCity.getText().toString().trim();
                //pincode = mPincode.getText().toString().trim();
                st_landmark = mLandmark.getText().toString().trim();

                Log.e("SAVED NUMBER :1:", ""+edit_contry.getText().toString() );
                if (firstCheck()) {
                    if (deliveryMobile.isEmpty()) {
                        saved_number = registerMobile;
                        myPreferences.setAddressEmailid(mContext, emailId);
                        hitStoreCustomerInfo((Activity) mContext, Const.APP_ID, registerMobile,
                                customerName, emailId, houseNumber, st_landmark, street, city, pincode,
                                saved_number);
                    }else {
                        saved_number = edit_contry.getText().toString() +""+ mDeliveryMobile.getText().toString();
                        myPreferences.setDELIVERY_MOBILE(mContext, saved_number);
                        hitStoreCustomerInfo((Activity) mContext, Const.APP_ID, registerMobile,
                                customerName, emailId, houseNumber, st_landmark, street, city, pincode,
                                myPreferences.getDELIVERY_MOBILE(mContext));
                    }

                }
            }catch (NullPointerException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    private boolean validateNum(Context mActivity, String mobile) {
        Boolean validate = false;
        Log.e("MOBILE LENGTH", ""+mobile.length());
        if ((mobile.length()<9)) {
            Toast.makeText(mActivity, getResources().getString(R.string.valid_mobile_no), Toast.LENGTH_SHORT).show();
            validate = false;
        }
        else if (!validCellPhone(mobile)) {
            Toast.makeText(mActivity, getResources().getString(R.string.valid_mobile_no), Toast.LENGTH_SHORT).show();
            validate = false;
        }
        else {
            validate = true;
        }
        return validate;
    }

    public boolean validCellPhone(String number){

        return android.util.Patterns.PHONE.matcher(number).matches();
    }

    private boolean firstCheck() {

        if (customerName.length() == 0) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.provide_username), Toast.LENGTH_SHORT).show();
            mCustomerName.requestFocus();
            return false;
        }
        if (!util.isOnlyLetters(customerName)) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.username_adrs), Toast.LENGTH_SHORT).show();
            mCustomerName.requestFocus();
            return false;

        }
        if (!deliveryMobile.isEmpty()) {
            if(!validateNum(mContext, deliveryMobile)){
                return false;
            }
        }
        if (!emailId.isEmpty() && !isValidEmail(emailId)) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.valid_mail_adrs), Toast.LENGTH_SHORT).show();
            mEmailID.requestFocus();
            return false;

        } else if (mEmailID.getText().toString().startsWith(" ") || mEmailID.getText().toString().endsWith(" ")) {
            if (mEmailID.getText().toString().trim().length() == 0) {
                Toast.makeText(mContext, mContext.getResources().getString(R.string.valid_mail_adrs), Toast.LENGTH_SHORT).show();
                mEmailID.requestFocus();
                return false;
            }
        }
        else if (st_landmark.length() == 0) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.strt_landmark), Toast.LENGTH_SHORT).show();
            mLandmark.requestFocus();
            return false;

        }else if (street.length() == 0) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.strt_adrs), Toast.LENGTH_SHORT).show();
            mStreet.requestFocus();
            return false;

        } else if (city.length() == 0) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.city_adrs), Toast.LENGTH_SHORT).show();
            mCity.requestFocus();
            return false;

        } else if (!util.isOnlyLetters(city)) {
            Toast.makeText(mContext, mContext.getResources().getString(R.string.city_accecpts_only_letters), Toast.LENGTH_SHORT).show();
            mCity.requestFocus();
            return false;

        }
        return true;
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }

    private void hitStoreCustomerInfo(final Activity mActivity, String appID, String register_mobile,
                                      String customer_name, String email_id, String house_number, String landmark,
                                      String street, String city, String pincode, String delivery_mobile) {

        try {

            if (Appstatus.getInstance(mActivity).isOnline()) {
                ProgressDialogUtils.startProgressBar(mActivity, getResources().getString(R.string.pleasewait));

                JSONObject setRequestCustomerAddress = JsonRequestAll
                        .createJsonParamsCustomerAddress(appID, register_mobile, customer_name,
                                email_id, house_number, landmark, street, city, pincode, delivery_mobile);

                Log.e(TAG + " REQUEST:::", "" + setRequestCustomerAddress);

                final WebServiceAbstract webServiceAbstract = new WebServiceAbstract() {
                    @Override
                    public void onComplete(JSONObject jsonObject) {
                        try {
                            if (jsonObject != null) {
                                Log.e(TAG, jsonObject.toString());
                                ProgressDialogUtils.stopProgress();
                                afterResponse(mActivity, jsonObject);
                            } else {
                                ProgressDialogUtils.stopProgress();
                                Toast.makeText(mActivity, getResources().getString(R.string.unabletoproceed), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                WebServiceClient.callPOSTService(ServiceURL.CUSTOMER_ADDRESS, setRequestCustomerAddress,
                        Const.JSON_OBJECT_RESPONSE, myPreferences.getAolToken(mActivity), webServiceAbstract, Request.Method.POST);

            } else {
                Toast.makeText(mActivity, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                ProgressDialogUtils.stopProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void afterResponse(Activity mActivity, JSONObject jsonObject) {

        try {
            Log.d(TAG, "RESPONSE : " + jsonObject);

            CustomerAddress customerAddress = new CustomerAddress(jsonObject);
            if (customerAddress.getStatus() == 0) {
                handleFailure(customerAddress.getMessage());
            } else {
                handleSuccess(customerAddress.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleSuccess(String message) {

        Log.d(TAG + " Address Stored :: ", message);

        util.openFragment(mContext, FragmentsName.CUSTOMER_DELIVERY_ADDRESS, FragmentsName.CUSTOMER_DELIVERY_ADDRESS_ID, null);

    }

    private void handleFailure(String message) {

        Log.d(TAG + " Address not Stored :: ", message);
        Toast.makeText(mContext, getResources().getString(R.string.unabletoproceed), Toast.LENGTH_SHORT).show();
    }
}
