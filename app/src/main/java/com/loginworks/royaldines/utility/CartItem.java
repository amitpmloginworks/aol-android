package com.loginworks.royaldines.utility;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;

/**
 * Created by abc on 18-Apr-17.
 */

public interface CartItem {
    void setCartItem(Activity mActivity, RecyclerView rv_checkout_cart, boolean reorder_page);

    void setCheckOutAddOn(Context mActivity, String id, RecyclerView rv_checkout_cart);
}
