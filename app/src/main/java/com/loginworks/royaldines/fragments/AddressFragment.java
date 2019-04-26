package com.loginworks.royaldines.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.loginworks.royaldines.R;
import com.loginworks.royaldines.activity.DashboardActivity;
import com.loginworks.royaldines.adapters.AddressAdapter;
import com.loginworks.royaldines.databasehandler.DataHandlerDB;
import com.loginworks.royaldines.extras.Appstatus;
import com.loginworks.royaldines.extras.Const;
import com.loginworks.royaldines.extras.FragmentsName;
import com.loginworks.royaldines.extras.JsonRequestAll;
import com.loginworks.royaldines.extras.MyPreferences;
import com.loginworks.royaldines.extras.ProgressDialogUtils;
import com.loginworks.royaldines.extras.ServiceURL;
import com.loginworks.royaldines.models.Address;
import com.loginworks.royaldines.services.WebServiceAbstract;
import com.loginworks.royaldines.services.WebServiceClient;
import com.loginworks.royaldines.utility.Util;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish Verma on 4/21/2017.
 */

public class AddressFragment extends Fragment {

    private String TAG = getClass().getSimpleName();
    private Activity mActivity;
    private Util util;
    private ArrayList<Address> addressList;
    private RecyclerView addressRecycler;
    private MyPreferences myPreferences;
    private AddressAdapter addressAdapter;
    private DataHandlerDB dataHandlerDB;
    private Button btnAddNewAddress;
    private TextView tvTitle;
    private SearchView searchView;
    private ImageView iv_location;
    private ImageView imv_editOrder;
    private RelativeLayout ll_Cart;
    private LinearLayout ll_no_adderess;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);

        mActivity = getActivity();
        myPreferences = MyPreferences.getInstance();
        dataHandlerDB = DataHandlerDB.getInstance();
        util = Util.getInstance();
        findViews(view);

        try {
            Bundle bundle = getArguments();
            if (bundle != null && bundle.containsKey("address")) {
                addressList = (ArrayList<Address>) getArguments().getSerializable("address");
                addressAdapter = new AddressAdapter(mActivity, addressList, ll_no_adderess, addressRecycler);
                addressRecycler.setAdapter(addressAdapter);
            } else {
                hitAddressData(mActivity, Const.APP_ID, myPreferences.getUserMobile(mActivity));
            }
        } catch (Exception ew) {
            ew.printStackTrace();
            hitAddressData(mActivity, Const.APP_ID, myPreferences.getUserMobile(mActivity));
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        tvTitle = ((DashboardActivity) mActivity).txt_Title;
        searchView = ((DashboardActivity) mActivity).searchview;
        iv_location = ((DashboardActivity) mActivity).imv_location;

        ll_Cart = ((DashboardActivity) mActivity).ll_cart;
        imv_editOrder = ((DashboardActivity) mActivity).imv_editOrder;
        ll_Cart.setVisibility(View.GONE);
        imv_editOrder.setVisibility(View.GONE);
        DashboardActivity.toolbar.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.deliver_info));
    }

    private void findViews(View view) {

        util.disableKeyboard(mActivity);
        addressRecycler = (RecyclerView) view.findViewById(R.id.addressRecycler);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mActivity);
        addressRecycler.setLayoutManager(linearLayoutManager);
        btnAddNewAddress = (Button) view.findViewById(R.id.btn_add_new_address);
        ll_no_adderess = (LinearLayout) view.findViewById(R.id.ll_no_adderess);

        btnAddNewAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("page", "deliveryInfo");
                util.openFragment(mActivity, FragmentsName.ADD_ADDRESS, FragmentsName.ADD_ADDRESS_ID, bundle);
            }
        });

    }

    private void hitAddressData(final Activity mActivity, String appId, String mobile_number) {

        try {
            if (Appstatus.getInstance(mActivity).isOnline()) {
                ProgressDialogUtils.startProgressBar(mActivity, getResources().getString(R.string.pleasewait));

                final JSONObject jsonObj = JsonRequestAll.createJsonParamsGetAddress(appId, mobile_number);
                Log.e(TAG + " REQUEST :: ", jsonObj + "");

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
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };

                WebServiceClient.callPOSTService(ServiceURL.GET_ADDRESSES, jsonObj, Const.JSON_OBJECT_RESPONSE,
                        myPreferences.getAolToken(mActivity), webServiceAbstract, Request.Method.POST);

            } else {
                Toast.makeText(mActivity, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                ProgressDialogUtils.stopProgress();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void afterResponse(Activity mActivity, JSONObject jsonObject) {

        Address address = new Address(jsonObject);
        if (address.getStatus() == 1) {
            dataHandlerDB.insertAddressData(address.getAddresses());
            addressList = address.getAddresses();
            addressAdapter = new AddressAdapter(mActivity, addressList, ll_no_adderess, addressRecycler);
            addressRecycler.setAdapter(addressAdapter);
        }
    }
}
