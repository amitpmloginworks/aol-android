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
import com.loginworks.royaldines.adapters.CategoriesAdapter;
import com.loginworks.royaldines.databasehandler.DataHandlerDB;
import com.loginworks.royaldines.extras.Appstatus;
import com.loginworks.royaldines.extras.Const;
import com.loginworks.royaldines.extras.FragmentsName;
import com.loginworks.royaldines.extras.JsonRequestAll;
import com.loginworks.royaldines.extras.MyPreferences;
import com.loginworks.royaldines.extras.ProgressDialogUtils;
import com.loginworks.royaldines.extras.ServiceURL;
import com.loginworks.royaldines.models.CategoriesModel;
import com.loginworks.royaldines.services.WebServiceAbstract;
import com.loginworks.royaldines.services.WebServiceClient;
import com.loginworks.royaldines.utility.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;


public class CategoriesFragment extends Fragment implements SearchView.OnQueryTextListener, View.OnClickListener {

    private String TAG = CategoriesFragment.this.getClass().getSimpleName();
    private MyPreferences myPreferences;
    private RecyclerView recyclerView;
    private LinearLayoutManager mLayoutManager;
    private CategoriesAdapter categoriesAdapter;
    private Activity mActivity;
    private ArrayList<CategoriesModel> categoriesModelArrayList;
    private SearchView searchView;
    private ImageView iv_location;
    private TextView tvTitle;
    private LinearLayout llNoSearch;
    private RelativeLayout ll_Cart;
    private TextView tv_cart_counter;
    private Util util;
    private DataHandlerDB dataHandlerDB;
    private ImageView imv_editOrder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories, container, false);

        mActivity = getActivity();
        myPreferences = MyPreferences.getInstance();
        dataHandlerDB = DataHandlerDB.getInstance();
        util = Util.getInstance();
        findViews(view);

        return view;
    }

    private void findViews(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.categoriesRecycler);
        mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        llNoSearch = (LinearLayout) view.findViewById(R.id.ll_search);
    }

    @Override
    public void onResume() {
        super.onResume();
        tvTitle = ((DashboardActivity) mActivity).txt_Title;
        tvTitle.setText(getResources().getString(R.string.categories));
        tvTitle.setVisibility(View.VISIBLE);
        DashboardActivity.toolbar.setVisibility(View.VISIBLE);
        setupSearchView();

        ll_Cart = ((DashboardActivity) mActivity).ll_cart;
        ll_Cart.setVisibility(View.VISIBLE);

        tv_cart_counter = ((DashboardActivity) mActivity).cart_counter;
        int totalQty = dataHandlerDB.getTotalQty();
        if (totalQty == 0) {
            tv_cart_counter.setVisibility(View.GONE);
        } else {
            tv_cart_counter.setVisibility(View.VISIBLE);
            tv_cart_counter.setText(totalQty + "");
        }

        iv_location = ((DashboardActivity) mActivity).imv_location;
        if (dataHandlerDB.getBranchCount() < 2) {
            iv_location.setVisibility(View.GONE);
        } else {
            iv_location.setVisibility(View.VISIBLE);
        }

        imv_editOrder = ((DashboardActivity) mActivity).imv_editOrder;

        imv_editOrder.setVisibility(View.GONE);


        ll_Cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dataHandlerDB.getTotalQty() > 0) {
                    util.openFragment(mActivity, FragmentsName.CHECKOUT, FragmentsName.CHECKOUT_ID, null);
                } else {
                    Toast.makeText(mActivity, getResources().getString(R.string.no_item_into_cart), Toast.LENGTH_SHORT).show();
                }

            }
        });

        if (Appstatus.getInstance(mActivity).isOnline()) {
            hitCategoriesTask(mActivity, myPreferences.getBranchId(mActivity));
        } else {
            Toast.makeText(mActivity, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // ll_Cart.setVisibility(View.GONE);
    }

    /*Get Categories based on Branch ID*/
    private void hitCategoriesTask(final Activity mActivity, String branchId) {

        try {
            if (Appstatus.getInstance(mActivity).isOnline()) {

                ProgressDialogUtils.startProgressBar(mActivity, getResources().getString(R.string.pleasewait));
                JSONObject setRequestCategory = JsonRequestAll.setRequestCategory(branchId);
                Log.e(TAG + "- REQUEST", setRequestCategory + "");

                WebServiceAbstract webServiceAbstract = new WebServiceAbstract() {
                    @Override
                    public void onComplete(JSONObject jsonObject) {
                        try {
                            if (jsonObject != null) {
                                ProgressDialogUtils.stopProgress();
                                afterResponse(mActivity, jsonObject);
                                Log.e(TAG + "- RESPONSE", jsonObject.toString());
                            } else {
                                ProgressDialogUtils.stopProgress();
//                                     Toast.makeText(activity, getString(R.string.try_again), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };
                WebServiceClient.callPOSTService(ServiceURL.CATEGORIES, setRequestCategory,
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
            CategoriesModel categoriesModel = new CategoriesModel(jsonObject);
            if (categoriesModel.getStatus().equalsIgnoreCase("1")) {
                handleSuccess(mActivity, categoriesModel);
            } else {
                handleFailure(mActivity, categoriesModel);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void handleFailure(Activity mActivity, CategoriesModel categoriesModel) {
        Toast.makeText(mActivity, categoriesModel.getMessage(), Toast.LENGTH_SHORT).show();
    }

    private void handleSuccess(Activity mActivity, CategoriesModel categoriesModel) {
        categoriesModelArrayList = categoriesModel.getCategoriesModelArrayList();

        if (categoriesModelArrayList != null && categoriesModelArrayList.size() > 0) {
            categoriesAdapter = new CategoriesAdapter(mActivity, categoriesModelArrayList);
            recyclerView.setAdapter(categoriesAdapter);
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String searchtext) {
        searchView.setBackgroundColor(getResources().getColor(R.color.colorPrimarytransparant));

        if (searchtext.length() == 0) {
            searchView.setBackgroundColor(getResources().getColor(android.R.color.transparent));
        }
        searchtext = searchtext.toLowerCase();

        ArrayList<CategoriesModel> filtered_Lst = new ArrayList<>();
        if ((categoriesModelArrayList != null) && (categoriesModelArrayList.size() > 0)) {
            for (int i = 0; i < categoriesModelArrayList.size(); i++) {
                String productName = categoriesModelArrayList.get(i).getName().toLowerCase();
                // boolean isContain = isContain(searchtext, productName);
                if (productName.contains(searchtext)) {
                    filtered_Lst.add(categoriesModelArrayList.get(i));

                    Log.e("Filter Size", filtered_Lst.size() + "");
                    Map<String, CategoriesModel> map = new LinkedHashMap<>();
                    for (CategoriesModel categoriesModel : filtered_Lst) {
                        map.put(categoriesModel.getCatid(), categoriesModel);
                    }

                    filtered_Lst.clear();
                    filtered_Lst.addAll(map.values());
                    filtered_Lst = new ArrayList<CategoriesModel>(new LinkedHashSet<CategoriesModel>(filtered_Lst));

                    if (recyclerView.getVisibility() == View.GONE) {
                        recyclerView.setVisibility(View.VISIBLE);
                        llNoSearch.setVisibility(View.GONE);
                    }
                    recyclerView.setLayoutManager(mLayoutManager);
                    categoriesAdapter = new CategoriesAdapter(mActivity, filtered_Lst);
                    recyclerView.setAdapter(categoriesAdapter);
                    categoriesAdapter.notifyDataSetChanged();
                }
            }
            if (filtered_Lst.size() <= 0) {
                if (recyclerView.getVisibility() == View.VISIBLE) {
                    recyclerView.setVisibility(View.GONE);
                    llNoSearch.setVisibility(View.VISIBLE);
                }
            }
        }
        return true;
    }

    private void setupSearchView() {
        try {

            searchView = ((DashboardActivity) getActivity()).searchview;
            searchView.setVisibility(View.VISIBLE);
            searchView.setOnSearchClickListener(this);
            searchView.setOnQueryTextListener(this);
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
                    ll_Cart.setVisibility(View.VISIBLE);
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
                ll_Cart.setVisibility(View.GONE);
                break;
        }
    }

}
