package com.loginworks.royaldines.utility;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.loginworks.royaldines.adapters.CheckOutAddOnAdapter;
import com.loginworks.royaldines.adapters.CheckOutCartAdapter;
import com.loginworks.royaldines.databasehandler.DataHandlerDB;
import com.loginworks.royaldines.models.AddOn;
import com.loginworks.royaldines.models.Food;

import java.util.ArrayList;

/**
 * Created by abc on 18-Apr-17.
 */

public class CartItemIMPL implements CartItem {

    private CheckOutCartAdapter mCheckOutCartAdapter;

    @Override
    public void setCartItem(Activity mActivity, RecyclerView rv_checkout_cart, boolean reOrder_page) {
        DataHandlerDB dataHandlerDB = DataHandlerDB.getInstance();
        ArrayList<Food> cartArrayList = dataHandlerDB.getCartData();
        if (cartArrayList != null && cartArrayList.size() > 0) {
            mCheckOutCartAdapter = new CheckOutCartAdapter(mActivity, cartArrayList, reOrder_page);
            rv_checkout_cart.setAdapter(mCheckOutCartAdapter);

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rv_checkout_cart.setLayoutManager(mLayoutManager);
            rv_checkout_cart.setItemAnimator(new DefaultItemAnimator());
        }
    }

    @Override
    public void setCheckOutAddOn(Context mActivity, String id, RecyclerView rv_checkout_addon) {
        DataHandlerDB dataHandlerDB = DataHandlerDB.getInstance();

        ArrayList<AddOn> addOnArrayList = dataHandlerDB.getCartWiseAddon(id);
        if (addOnArrayList != null) {
            CheckOutAddOnAdapter mCheckOutAddOnAdapter = new CheckOutAddOnAdapter(mActivity, addOnArrayList);
            rv_checkout_addon.setAdapter(mCheckOutAddOnAdapter);

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rv_checkout_addon.setLayoutManager(mLayoutManager);
        }
    }
}
