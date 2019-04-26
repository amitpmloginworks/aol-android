package com.loginworks.royaldines.utility;

import android.widget.TextView;

import com.loginworks.royaldines.databasehandler.DataHandlerDB;

/**
 * Created by abc on 17-Apr-17.
 */

public class CartRefreshIMPL implements CartRefresh {

    @Override
    public void setCartQty(TextView tv_total) {
        DataHandlerDB dataHandlerDB = DataHandlerDB.getInstance();
        int cartqty = dataHandlerDB.getTotalQty();
        tv_total.setText(cartqty + "");
    }

    @Override
    public void setCartAmt(TextView tv_item_count) {
            DataHandlerDB dataHandlerDB = DataHandlerDB.getInstance();
            double grandtotal = dataHandlerDB.getTotalAmount();
            tv_item_count.setText("SR " + grandtotal + " /-");
    }
}
