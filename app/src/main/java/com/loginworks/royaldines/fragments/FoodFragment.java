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
import com.loginworks.royaldines.adapters.FoodAdapter;
import com.loginworks.royaldines.databasehandler.DataHandlerDB;
import com.loginworks.royaldines.extras.Appstatus;
import com.loginworks.royaldines.extras.Const;
import com.loginworks.royaldines.extras.FragmentsName;
import com.loginworks.royaldines.extras.JsonRequestAll;
import com.loginworks.royaldines.extras.MyPreferences;
import com.loginworks.royaldines.extras.ProgressDialogUtils;
import com.loginworks.royaldines.extras.ServiceURL;
import com.loginworks.royaldines.models.AddOn;
import com.loginworks.royaldines.models.Food;
import com.loginworks.royaldines.services.WebServiceAbstract;
import com.loginworks.royaldines.services.WebServiceClient;
import com.loginworks.royaldines.utility.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;

/**
 * Created by abc on 10-Apr-17.
 */

public class FoodFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener {

    public static TextView tv_total_amount, tv_item_count;
    private RecyclerView rv_food;
    private FoodAdapter mFoodAdapter;
    private Activity mActivity;
    private MyPreferences myPreferences;
    private String TAG = FoodFragment.class.getSimpleName();
    private DataHandlerDB dataHandlerDB;
    private LinearLayout mll_TotalAmount;
    private Food food;
    private Util util;
    private int totalQty = 0;

    private double totalamount = 0;
    private SearchView searchView;
    private TextView tvTitle;
    private ArrayList<Food> mFoodArrayList;
    private HashMap<String, ArrayList<AddOn>> addonHashMap;
    // private ImageView ivLocation;
    private ImageView imv_editOrder, iv_location;
    private LinearLayout ll_search;
    private RelativeLayout ll_Cart;
    private LinearLayoutManager mLayoutManager;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.food_fragment, container, false);
        mActivity = getActivity();
        myPreferences = MyPreferences.getInstance();
        dataHandlerDB = DataHandlerDB.getInstance();
        util = Util.getInstance();
        findViews(view);

        String catId = getArguments().getString(Const.CATEGORY_KEY);
        hitFoodTask(mActivity, myPreferences.getBranchId(mActivity), catId);

        try {
            totalQty = dataHandlerDB.getTotalQty();
            totalamount = dataHandlerDB.getTotalAmount();
            tv_total_amount.setText("SR " + totalamount + " /-");
            tv_item_count.setText(totalQty + "");
        } catch (Exception ex) {

        }
        mll_TotalAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_item_count.getText().toString().equalsIgnoreCase("0")) {
                    Toast.makeText(mActivity, getResources().getString(R.string.no_item_into_cart), Toast.LENGTH_SHORT).show();
                    return;
                }
                util.openFragment(mActivity, FragmentsName.CHECKOUT, FragmentsName.CHECKOUT_ID, null);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        DashboardActivity.toolbar.setVisibility(View.VISIBLE);
        setupSearchView();
        tvTitle = ((DashboardActivity) mActivity).txt_Title;
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getResources().getString(R.string.food_menu));

        ll_Cart = ((DashboardActivity) mActivity).ll_cart;
        ll_Cart.setVisibility(View.GONE);

        imv_editOrder = ((DashboardActivity) mActivity).imv_editOrder;
        imv_editOrder.setVisibility(View.GONE);

        iv_location = ((DashboardActivity) mActivity).imv_location;
        if (dataHandlerDB.getBranchCount() < 2) {
            iv_location.setVisibility(View.GONE);
        } else {
            iv_location.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void findViews(View view) {
        rv_food = (RecyclerView) view.findViewById(R.id.rv_food);
        tv_total_amount = (TextView) view.findViewById(R.id.tv_cart_amount);
        tv_item_count = (TextView) view.findViewById(R.id.tv_item_count);
        mll_TotalAmount = (LinearLayout) view.findViewById(R.id.ll_total_cart);
        ll_search = (LinearLayout) view.findViewById(R.id.ll_search);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
    }

    private void hitFoodTask(final Activity mActivity, String branchId, String catId) {
        try {
            if (Appstatus.getInstance(mActivity).isOnline()) {

                ProgressDialogUtils.startProgressBar(mActivity, getResources().getString(R.string.pleasewait));
                JSONObject setRequestFood = JsonRequestAll.setRequestFood(branchId, catId);
                Log.e("Food REQUEST", setRequestFood.toString());

                WebServiceAbstract webServiceAbstract = new WebServiceAbstract() {
                    @Override
                    public void onComplete(JSONObject jsonObject) {
                        try {
                            if (jsonObject != null) {
                                ProgressDialogUtils.stopProgress();
                                afterResponse(jsonObject);
                                Log.e("Food RESPONSE", jsonObject.toString());
                            } else {
                                ProgressDialogUtils.stopProgress();
                                //Toast.makeText(activity, getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };

                WebServiceClient.callPOSTService(ServiceURL.FOOD, setRequestFood,
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
            food = new Food(jsonObject);
            if (food.getStatus().equalsIgnoreCase("1")) {
                handleSuccess(food);
            } else {
                handleFailure(food.getMessage());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleFailure(String message) {
        Toast.makeText(mActivity, message, Toast.LENGTH_SHORT).show();
    }

    private void handleSuccess(Food food) {
        mFoodArrayList = food.getFoodArrayList();
        addonHashMap = food.getAddonHashMap();
        if (mFoodArrayList.size() > 0 && addonHashMap != null) {
            mFoodAdapter = new FoodAdapter(mActivity, mFoodArrayList, addonHashMap);
            rv_food.setLayoutManager(mLayoutManager);
            rv_food.setAdapter(mFoodAdapter);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
//        tvTitle.setVisibility(View.VISIBLE);
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchtext) {
//        tvTitle.setVisibility(View.GONE);
        searchView.setBackgroundColor(getResources().getColor(R.color.colorPrimarytransparant));

        if (searchtext.length() == 0) {
            searchView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }

        searchtext = searchtext.toLowerCase();

        ArrayList<Food> filtered_Lst = new ArrayList<>();
        if ((mFoodArrayList != null) && (mFoodArrayList.size() > 0)) {
            for (int i = 0; i < mFoodArrayList.size(); i++) {
                String productName = mFoodArrayList.get(i).getProduct_name().toLowerCase();
                if (productName.contains(searchtext)) {
                    filtered_Lst.add(mFoodArrayList.get(i));

                    Map<String, Food> map = new LinkedHashMap<>();
                    for (Food food : filtered_Lst) {
                        map.put(food.getProduct_id(), food);
                    }

                    filtered_Lst.clear();
                    filtered_Lst.addAll(map.values());
                    filtered_Lst = new ArrayList<Food>(new LinkedHashSet<Food>(filtered_Lst));

                    if (rv_food.getVisibility() == View.GONE) {
                        rv_food.setVisibility(View.VISIBLE);
                        ll_search.setVisibility(View.GONE);
                    }

                    rv_food.setLayoutManager(mLayoutManager);
                    mFoodAdapter = new FoodAdapter(mActivity, filtered_Lst, addonHashMap);
                    rv_food.setAdapter(mFoodAdapter);
                    mFoodAdapter.notifyDataSetChanged();

                }
            }
            if (filtered_Lst.size() <= 0) {
                if (rv_food.getVisibility() == View.VISIBLE) {
                    rv_food.setVisibility(View.GONE);
                    ll_search.setVisibility(View.VISIBLE);
                }
            }
        }
        return true;
    }

    private void setupSearchView() {
        try {
            searchView = ((DashboardActivity) getActivity()).searchview;
            searchView.setVisibility(View.VISIBLE);
            searchView.setOnQueryTextListener(this);
            searchView.setOnSearchClickListener(this);
            searchView.setSubmitButtonEnabled(false);
            searchView.onActionViewCollapsed();
            searchView.setQueryHint(getResources().getString(R.string.search_here));
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) searchView.getLayoutParams();
            marginParams.setMargins(0, 20, 0, 20);

            ImageView closeButton = (ImageView) searchView.findViewById(R.id.search_close_btn);

            // Set on click listener
            closeButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    //Clear query
                    searchView.setQuery("", false);
                    searchView.setIconifiedByDefault(true);
                    //Collapse the action view
                    searchView.onActionViewCollapsed();
                    //Collapse the search widget
                    tvTitle.setVisibility(View.VISIBLE);
                }
            });
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {


        switch (v.getId()) {

            case R.id.searchview:

                tvTitle.setVisibility(View.GONE);

                break;
        }
    }
}
