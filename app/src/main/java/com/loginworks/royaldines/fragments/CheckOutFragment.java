package com.loginworks.royaldines.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.loginworks.royaldines.R;
import com.loginworks.royaldines.activity.DashboardActivity;
import com.loginworks.royaldines.databasehandler.DataHandlerDB;
import com.loginworks.royaldines.extras.Appstatus;
import com.loginworks.royaldines.extras.Const;
import com.loginworks.royaldines.extras.FragmentsName;
import com.loginworks.royaldines.extras.JsonRequestAll;
import com.loginworks.royaldines.extras.MyPreferences;
import com.loginworks.royaldines.extras.ProgressDialogUtils;
import com.loginworks.royaldines.extras.ServiceURL;
import com.loginworks.royaldines.models.Address;
import com.loginworks.royaldines.models.DeliveryDetail;
import com.loginworks.royaldines.models.VerifyOTP;
import com.loginworks.royaldines.services.WebServiceAbstract;
import com.loginworks.royaldines.services.WebServiceClient;
import com.loginworks.royaldines.utility.CartItem;
import com.loginworks.royaldines.utility.CartItemIMPL;
import com.loginworks.royaldines.utility.CartRefresh;
import com.loginworks.royaldines.utility.CartRefreshIMPL;
import com.loginworks.royaldines.utility.Util;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CheckOutFragment extends Fragment implements View.OnClickListener {

    private static final int FRAMEWORK_REQUEST_CODE = 1;
    public static TextView tv_check_amount;
    private final Map<Integer, OnCompleteListener> permissionsListeners = new HashMap<>();
    private int nextPermissionsRequestCode = 4000;
    private Activity mActivity;
    private RecyclerView rv_checkout_cart;
    private Button btn_ChekOut;
    private Util util;
    private MyPreferences myPreferences;
    private AlertDialog mAlertDailog;
    private DataHandlerDB dataHandlerDB;
    private TextView tvTitle;
    private SearchView searchView;
    private ImageView iv_Location;
    private ImageView imv_editOrder;
    private boolean reorder_page = false;
    private RelativeLayout ll_Cart;
    private AlertDialog alert;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_check_out, container, false);

        mActivity = getActivity();
        util = Util.getInstance();
        myPreferences = MyPreferences.getInstance();
        dataHandlerDB = DataHandlerDB.getInstance();

        findViews(view);
        setViews();

        myPreferences.setReorder(mActivity, false);

        return view;
    }

    private void findViews(View view) {
        DashboardActivity.toolbar.setVisibility(View.VISIBLE);

        rv_checkout_cart = (RecyclerView) view.findViewById(R.id.rv_checkout_cart);
        tv_check_amount = (TextView) view.findViewById(R.id.tv_check_amount);
        btn_ChekOut = (Button) view.findViewById(R.id.btn_CheckOut);
        btn_ChekOut.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        DashboardActivity.toolbar.setVisibility(View.VISIBLE);
        tvTitle = ((DashboardActivity) mActivity).txt_Title;
        searchView = ((DashboardActivity) mActivity).searchview;
        iv_Location = ((DashboardActivity) mActivity).imv_location;
        ll_Cart = ((DashboardActivity) mActivity).ll_cart;
        imv_editOrder = ((DashboardActivity) mActivity).imv_editOrder;
        ll_Cart.setVisibility(View.GONE);
        searchView.setVisibility(View.GONE);
        iv_Location.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.cart));
        tvTitle.setVisibility(View.VISIBLE);
        imv_editOrder.setVisibility(View.GONE);
    }

    private void setViews() {
        try {
            Bundle bundle = getArguments();
            if (bundle != null) {
                reorder_page = bundle.getBoolean("reorder");
            }
            CartItem cartItem = new CartItemIMPL();
            cartItem.setCartItem(mActivity, rv_checkout_cart, reorder_page);

            CartRefresh cartRefresh = new CartRefreshIMPL();
            cartRefresh.setCartAmt(tv_check_amount);
        }catch (IllegalStateException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_CheckOut:
                hitDeliveryTask(mActivity, myPreferences.getBranchId(mActivity));
                break;
        }
    }

    private void hitDeliveryTask(final Activity mActivity, String branchId) {
        try {
            if (Appstatus.getInstance(mActivity).isOnline()) {

                ProgressDialogUtils.startProgressBar(mActivity, getResources().getString(R.string.pleasewait));
                JSONObject setRequestDelivery = JsonRequestAll.setRequestDeliveryDetails(branchId);
                Log.e("Delivery REQUEST", setRequestDelivery.toString());

                WebServiceAbstract webServiceAbstract = new WebServiceAbstract() {
                    @Override
                    public void onComplete(JSONObject jsonObject) {
                        try {
                            if (jsonObject != null) {
                                ProgressDialogUtils.stopProgress();
                                afterResponse(jsonObject);
                                Log.e("Delivery RESPONSE", jsonObject.toString());
                            } else {
                                ProgressDialogUtils.stopProgress();
                                Toast.makeText(mActivity, getResources().getString(R.string.tryagain), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };

                WebServiceClient.callPOSTService(ServiceURL.DELIVERY_DETAILS, setRequestDelivery,
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
        try {
            DeliveryDetail deliveryDetail = new DeliveryDetail(jsonObject);
            if (deliveryDetail.getMessage().equalsIgnoreCase("Success")) {
                handleSuccess(deliveryDetail);
            } else {
                handleFailure(deliveryDetail.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleFailure(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    private void handleSuccess(final DeliveryDetail deliveryDetail) {

        if (deliveryDetail.getHomeDelivery().equalsIgnoreCase("1") && deliveryDetail.getTakeAway().equalsIgnoreCase("1")) {

            myPreferences.setOtpSupport(mActivity, deliveryDetail.getOtpFailContact());
            Log.e("USERNAME ", ""+myPreferences.getUserName(mActivity));
            Log.e("MOBILE ", ""+myPreferences.getUserMobile(mActivity));

            if(myPreferences.getUserMobile(mActivity).equalsIgnoreCase("")) {
                showInstructionsDialog(deliveryDetail);
            }else{
                showDeliveryOption(mActivity, deliveryDetail);
            }


        }
    }

    private void showDeliveryOption(final Activity mActivity, DeliveryDetail deliveryDetail){
        AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(mActivity);
        LayoutInflater inflater = mActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.delivery_dialog, null);
        mAlertBuilder.setView(dialogView);

        mAlertBuilder.setCancelable(true);

        mAlertDailog = mAlertBuilder.create();
        LinearLayout llTakeAway = (LinearLayout) dialogView.findViewById(R.id.ll_takeaway);
        LinearLayout llHomedelivery = (LinearLayout) dialogView.findViewById(R.id.ll_homedelivery);

        myPreferences.setServiceCharge(mActivity, deliveryDetail.getServiceCharge());
        myPreferences.setPackingCharge(mActivity, deliveryDetail.getPackCharge());
        myPreferences.setTaxStatus(mActivity, deliveryDetail.isTaxStaus());
        Log.e("Tax Staus:::", myPreferences.isTaxStatus(mActivity) + "");
        myPreferences.setCGST_TaxValue(mActivity, deliveryDetail.getCgst());
        myPreferences.setSGST_TaxValue(mActivity, deliveryDetail.getSgst());

        llTakeAway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                util.setDelivery(false);
                myPreferences.setTakeAway(mActivity, "1");
                myPreferences.setHomeDelivery(mActivity, "0");

                util.openFragment(mActivity, FragmentsName.CONFIRM_ORDER, FragmentsName.CONFIRM_ORDER_ID, null);

                if (mAlertDailog != null && mAlertDailog.isShowing())
                    mAlertDailog.dismiss();
            }
        });

        llHomedelivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    util.setDelivery(true);
                    double totalAmount = dataHandlerDB.getTotalAmount();

                    double min_order = 30.0;
                    //int min_order = Integer.parseInt(myPreferences.getMinOrder(mActivity));
                    if (min_order > totalAmount) {
                       // Toast.makeText(mActivity, getResources().getString(R.string.min_order_one) + (min_order - totalAmount) + getResources().getString(R.string.min_order_two), Toast.LENGTH_LONG).show();
                        Toast.makeText(mActivity, getResources().getString(R.string.min_order_one) + min_order + getResources().getString(R.string.min_order_two), Toast.LENGTH_LONG).show();
                        return;
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                myPreferences.setHomeDelivery(mActivity, "1");
                myPreferences.setTakeAway(mActivity, "0");

                if( !myPreferences.getUserMobile(mActivity).equalsIgnoreCase("")){
                    hitAddressTask(mActivity, myPreferences.getUserMobile(mActivity));
                }

                if (mAlertDailog != null && mAlertDailog.isShowing())
                    mAlertDailog.dismiss();
            }
        });
        if (mAlertDailog != null && !mAlertDailog.isShowing())
            mAlertDailog.show();

    }

    private void showInstructionsDialog(final DeliveryDetail deliveryDetail) {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        final LayoutInflater inflater = mActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_username, null);
        builder.setView(dialogView);
        builder.setCancelable(true);

        alert = builder.create();
        alert.setCanceledOnTouchOutside(false);
        final EditText editUserName = (EditText) dialogView.findViewById(R.id.edit_name);
        final EditText edit_contry = (EditText) dialogView.findViewById(R.id.edit_contry);
        final EditText editMobile = (EditText) dialogView.findViewById(R.id.edit_mobile);

        edit_contry.setEnabled(false);
        edit_contry.setKeyListener(null);
        edit_contry.setFocusable(false);

        Button btnSubmitUserName = (Button) dialogView.findViewById(R.id.btnSubmitUserName);

        btnSubmitUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = editUserName.getText().toString().trim();
                String contry = edit_contry.getText().toString().trim();
                String mobile = editMobile.getText().toString().trim();

                if(validateFields(username, mobile)) {

                    myPreferences.setUserName(mActivity, username);
                    myPreferences.setUserMobile(mActivity, contry +""+mobile);
                    //myPreferences.setDELIVERY_MOBILE(mActivity, contry +""+mobile);
                    Log.e("SET MOBILE ::", ""+myPreferences.getUserMobile(mActivity));
                    alert.dismiss();
                    showDeliveryOption(mActivity, deliveryDetail);
                }
            }
        });

        if (alert != null) {
            alert.show();
        }

    }

    private boolean validateFields(String username, String mobile) {
        Boolean validate = false;
        Log.e("MOBILE LENGTH", ""+mobile.length());
        if (username.length() == 0) {
            Toast.makeText(mActivity, getResources().getString(R.string.provide_username), Toast.LENGTH_SHORT).show();
            validate = false;
        }
        else if (!util.isOnlyLetters(username.trim())) {
            Toast.makeText(mActivity, getResources().getString(R.string.username_adrs), Toast.LENGTH_SHORT).show();
            validate = false;
        }
        else if((mobile.equalsIgnoreCase(""))) {
            Toast.makeText(mActivity, getResources().getString(R.string.provide_mobile), Toast.LENGTH_SHORT).show();
            validate = false;
        }
        else if ((mobile.length()<9)) {
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



    private void requestPermissions(
            final String permission,
            final int rationaleTitleResourceId,
            final int rationaleMessageResourceId,
            final OnCompleteListener listener) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (listener != null) {
                listener.onComplete();
            }
            return;
        }

        checkRequestPermissions(
                permission,
                rationaleTitleResourceId,
                rationaleMessageResourceId,
                listener);
    }

    @TargetApi(23)
    private void checkRequestPermissions(
            final String permission,
            final int rationaleTitleResourceId,
            final int rationaleMessageResourceId,
            final OnCompleteListener listener) {
        if (mActivity.checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
            if (listener != null) {
                listener.onComplete();
            }
            return;
        }

        final int requestCode = nextPermissionsRequestCode++;
        permissionsListeners.put(requestCode, listener);

        if (shouldShowRequestPermissionRationale(permission)) {
            new android.app.AlertDialog.Builder(mActivity)
                    .setTitle(rationaleTitleResourceId)
                    .setMessage(rationaleMessageResourceId)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            requestPermissions(new String[]{permission}, requestCode);
                        }
                    })
                    .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(final DialogInterface dialog, final int which) {
                            // ignore and clean up the listener
                            permissionsListeners.remove(requestCode);
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            requestPermissions(new String[]{permission}, requestCode);
        }
    }

    @TargetApi(23)
    @SuppressWarnings("unused")
    @Override
    public void onRequestPermissionsResult(final int requestCode,
                                           final @NonNull String permissions[],
                                           final @NonNull int[] grantResults) {
        final OnCompleteListener permissionsListener = permissionsListeners.remove(requestCode);
        if (permissionsListener != null
                && grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            permissionsListener.onComplete();
        }
    }

    private void hitAddressTask(final Activity mActivity, String mobile) {
        try {
            if (Appstatus.getInstance(mActivity).isOnline()) {

                ProgressDialogUtils.startProgressBar(mActivity, getResources().getString(R.string.pleasewait));
                JSONObject setRequestDelivery = JsonRequestAll.setRequestAddressDetails(mobile);
                Log.e("Addess REQUEST", setRequestDelivery.toString());

                WebServiceAbstract webServiceAbstract = new WebServiceAbstract() {
                    @Override
                    public void onComplete(JSONObject jsonObject) {
                        try {
                            if (jsonObject != null) {
                                ProgressDialogUtils.stopProgress();
                                afterAddressResponse(jsonObject);
                                Log.e("Adress RESPONSE", jsonObject.toString());
                            } else {
                                ProgressDialogUtils.stopProgress();
                                Toast.makeText(mActivity, getResources().getString(R.string.tryagain), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };

                WebServiceClient.callPOSTService(ServiceURL.ADDRESS, setRequestDelivery,
                        Const.JSON_OBJECT_RESPONSE, myPreferences.getAolToken(mActivity), webServiceAbstract, Request.Method.POST);

            } else {
                Toast.makeText(mActivity, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                ProgressDialogUtils.stopProgress();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void afterAddressResponse(JSONObject jsonObject) {
        try {
            VerifyOTP verifyOTP = new VerifyOTP(jsonObject);
            if (verifyOTP.getStatus() == 1) {

                myPreferences.setSession(mActivity);

                Bundle bundle = new Bundle();
                bundle.putString("page", "otp");
                if (myPreferences.getHomeDelivery(mActivity).equals("1")) {
                    if (verifyOTP.getAddress().length() > 0) {
                        Address address = new Address(verifyOTP.getAddress());
                        dataHandlerDB.insertAddressData(address.getAddresses());
                        util.openFragment(mActivity, FragmentsName.CUSTOMER_DELIVERY_ADDRESS, FragmentsName.CUSTOMER_DELIVERY_ADDRESS_ID, bundle);
                    } else {
                        util.openFragment(mActivity, FragmentsName.ADD_ADDRESS, FragmentsName.ADD_ADDRESS_ID, bundle);
                    }
                }
//                } else {
//                    util.openFragment(mActivity, FragmentsName.CONFIRM_ORDER, FragmentsName.CONFIRM_ORDER_ID, bundle);
//                }
            } else {
                Toast.makeText(mActivity, verifyOTP.getMessage(), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleAddressFailure(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    private interface OnCompleteListener {
        void onComplete();
    }
}
