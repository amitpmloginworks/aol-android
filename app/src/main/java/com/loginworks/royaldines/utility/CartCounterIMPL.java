package com.loginworks.royaldines.utility;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loginworks.royaldines.databasehandler.DataHandlerDB;


public class CartCounterIMPL implements CartCounter {

    @Override
    public void setCounter(RelativeLayout rl_counter, TextView tv_counter) {
        DataHandlerDB dataHandlerDB = DataHandlerDB.getInstance();
        int totalQty = dataHandlerDB.getTotalQty();
        if (totalQty == 0) {
            rl_counter.setVisibility(View.GONE);
        } else {
            rl_counter.setVisibility(View.VISIBLE);
            tv_counter.setText(String.valueOf(totalQty));
        }
    }
}
