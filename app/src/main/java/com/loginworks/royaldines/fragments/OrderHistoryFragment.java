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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.loginworks.royaldines.R;
import com.loginworks.royaldines.activity.DashboardActivity;
import com.loginworks.royaldines.adapters.OrderHistoryAdapter;
import com.loginworks.royaldines.extras.Appstatus;
import com.loginworks.royaldines.extras.Const;
import com.loginworks.royaldines.extras.JsonRequestAll;
import com.loginworks.royaldines.extras.MyPreferences;
import com.loginworks.royaldines.extras.ProgressDialogUtils;
import com.loginworks.royaldines.extras.ServiceURL;
import com.loginworks.royaldines.models.OrderHistory;
import com.loginworks.royaldines.services.WebServiceAbstract;
import com.loginworks.royaldines.services.WebServiceClient;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ujjwal on 4/11/2017.
 */

public class OrderHistoryFragment extends Fragment {

    private String TAG = getClass().getSimpleName();
    private Activity mActivity;
    private RecyclerView mRecyclerView;
    private ArrayList<OrderHistory> historyList;
    private OrderHistoryAdapter orderHistoryAdapter;
    private LinearLayoutManager linearLayoutManager;
    private TextView tvTitle;
    private ImageView ivLocation;
    private SearchView searchView;
    private LinearLayout ll_no_record;
    private RelativeLayout ll_Cart;

    private MyPreferences myPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_history, container, false);
        mActivity = getActivity();
        myPreferences = MyPreferences.getInstance();

        findViews(view);

        if (!myPreferences.getUserMobile(mActivity).equals("")) {
            hitOrderHistoryTask(mActivity, myPreferences.getUserMobile(mActivity));
        } else {
            mActivity.finish();
            Toast.makeText(mActivity, mActivity.getResources().getString(R.string.not_logged_id), Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    private void findViews(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        linearLayoutManager = new LinearLayoutManager(mActivity);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        ll_no_record = (LinearLayout) view.findViewById(R.id.ll_no_record);
    }

    @Override
    public void onResume() {
        super.onResume();

        DashboardActivity.toolbar.setVisibility(View.VISIBLE);
        tvTitle = ((DashboardActivity) mActivity).txt_Title;
        ivLocation = ((DashboardActivity) mActivity).imv_location;
        ll_Cart = ((DashboardActivity) mActivity).ll_cart;
        ll_Cart.setVisibility(View.GONE);
        ivLocation.setVisibility(View.GONE);
        searchView = ((DashboardActivity) mActivity).searchview;
        searchView.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.order_history));
    }

    private void hitOrderHistoryTask(final Activity mActivity, String mobile) {

        try {
            if (Appstatus.getInstance(mActivity).isOnline()) {

                ProgressDialogUtils.startProgressBar(mActivity, getResources().getString(R.string.pleasewait));
                JSONObject setRequestCategory = JsonRequestAll.setRequestOrderHistory(mobile);
                Log.e("Order history REQUEST", setRequestCategory + "");

                WebServiceAbstract webServiceAbstract = new WebServiceAbstract() {
                    @Override
                    public void onComplete(JSONObject jsonObject) {
                        try {
                            if (jsonObject != null) {
                                ProgressDialogUtils.stopProgress();
                                afterResponse(mActivity, jsonObject);
                                Log.e(TAG, jsonObject.toString());
                            } else {
                                ProgressDialogUtils.stopProgress();
                                Toast.makeText(mActivity, getResources().getString(R.string.no_order), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };
                WebServiceClient.callPOSTService(ServiceURL.ORDER_HISTORY, setRequestCategory,
                        Const.JSON_OBJECT_RESPONSE, myPreferences.getAolToken(mActivity), webServiceAbstract, Request.Method.POST);
            } else {
                Toast.makeText(mActivity, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                ProgressDialogUtils.stopProgress();

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(mActivity, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
            ProgressDialogUtils.stopProgress();
        }
    }

    private void afterResponse(final Activity mActivity, final JSONObject jsonObject) {

        try {
            Log.d(TAG, "RESPONSE :: " + jsonObject);

            OrderHistory orderHistory = new OrderHistory(jsonObject);
            if (orderHistory.getErrorMessage() != null) {
                handleFailure(mActivity, orderHistory);
            } else {
                handleSuccess(mActivity, orderHistory);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(mActivity, getResources().getString(R.string.no_order), Toast.LENGTH_SHORT).show();
        }
    }

    private void handleFailure(Activity mActivity, OrderHistory branchParser) {
        Toast.makeText(mActivity, branchParser.getErrorMessage(), Toast.LENGTH_SHORT).show();
    }

    private void handleSuccess(Activity mActivity, OrderHistory orderHistory) {
        Log.d(TAG, "ORDER HISTORY LIST SUCCESS");

        if (orderHistory.getOrderHistoryList().size() > 0) {
            orderHistoryAdapter = new OrderHistoryAdapter(mActivity, orderHistory.getOrderHistoryList());
            mRecyclerView.setAdapter(orderHistoryAdapter);
        } else {
            ll_no_record.setVisibility(View.VISIBLE);
            //Toast.makeText(mActivity, getResources().getString(R.string.no_order), Toast.LENGTH_SHORT).show();
        }
    }
}
