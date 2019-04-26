package com.loginworks.royaldines.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loginworks.royaldines.R;

import com.loginworks.royaldines.models.FlatDeals;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by abc on 04-Apr-17.
 */

public class FlatDealAdapter extends PagerAdapter {


    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<FlatDeals> flatdealsArrayList;

    public FlatDealAdapter(Context mContext, ArrayList<FlatDeals> flatdealsArrayList) {
        this.mContext = mContext;
        this.flatdealsArrayList = flatdealsArrayList;
    }


    @Override
    public int getCount() {
        return flatdealsArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        FlatDeals flatDeals = flatdealsArrayList.get(position);
        TextView tv_flate_name, tv_flate_description, tv_flate_date;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(R.layout.home_flatdeals_row, container, false);

        tv_flate_name = (TextView) view.findViewById(R.id.tv_flate_name);
        tv_flate_description = (TextView) view.findViewById(R.id.tv_flate_description);
        tv_flate_date = (TextView) view.findViewById(R.id.tv_flate_date);

        String discount = flatDeals.getDiscount();
        if (discount != null && !discount.equalsIgnoreCase("")) {
            tv_flate_name.setText(flatDeals.getDiscount() + "% OFF");
        }
        tv_flate_description.setText(flatDeals.getDescription());

        String flatDate = flatDeals.getEnd_date();

        DateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat formatter = new SimpleDateFormat("dd MMM, yyyy");

        Date convertedDate = null;
        try {
            convertedDate = parser.parse(flatDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String output = formatter.format(convertedDate);
        tv_flate_date.setText("Valid Till " + output);
        ((ViewPager) container).addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((LinearLayout) object);
    }
}
