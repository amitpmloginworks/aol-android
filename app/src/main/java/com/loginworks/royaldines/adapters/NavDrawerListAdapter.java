package com.loginworks.royaldines.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loginworks.royaldines.R;
import com.loginworks.royaldines.extras.MyPreferences;
import com.loginworks.royaldines.models.NavDrawerItem;

import java.util.ArrayList;


public class NavDrawerListAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NavDrawerItem> navDrawerItems;

    private int selectedItem = 2;
    private LayoutInflater mInflater;

    private MyPreferences myPreferences;

    public NavDrawerListAdapter(Context context, ArrayList<NavDrawerItem> navDrawerItems, int selectedItem) {
        this.context = context;
        this.navDrawerItems = navDrawerItems;
        this.selectedItem = selectedItem;
        myPreferences = MyPreferences.getInstance();
        mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return navDrawerItems.size();
    }

    @Override
    public Object getItem(int position) {
        return navDrawerItems.get(position);
    }

    public void setSelectedItem(int pos) {
//        notifyDataSetChanged();
        selectedItem = pos;
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        MyHolder myHolder;

        if (position == 0) {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.drawer_header_item, null);
                myHolder = new MyHolder(convertView, position);
                convertView.setTag(myHolder);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }

            String username = myPreferences.getUserName(context);
            String mobile = myPreferences.getUserMobile(context);
            if (username != null && mobile != null) {
                myHolder.txtMobile.setText(mobile);
                myHolder.txtUserName.setText(username);
            } else {
                myHolder.txtUserName.setText(context.getResources().getString(R.string.guest_user));
                myHolder.txtMobile.setVisibility(View.GONE);
            }

        } else {
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.drawer_list_item, null);
                myHolder = new MyHolder(convertView, position);
                convertView.setTag(myHolder);
            } else {
                myHolder = (MyHolder) convertView.getTag();
            }

            myHolder.txtTitle.setText(navDrawerItems.get(position).getTitle());

//        if (navDrawerItems.get(position).getCounterVisibility()) {
//            txtCount.setText(navDrawerItems.get(position).getCount());
//        } else {
//            txtCount.setVisibility(View.GONE);
//        }

            if (position == selectedItem && position < 4) {
                myHolder.menuLayout.setBackgroundColor(context.getResources().getColor(R.color.menu_list_selected_color));
                myHolder.txtTitle.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                myHolder.imgIcon.setImageResource(navDrawerItems.get(position).getSelectedIcon());
            } else {
                myHolder.menuLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
                myHolder.txtTitle.setTextColor(context.getResources().getColor(R.color.menu_list_unselected_color));
                myHolder.imgIcon.setImageResource(navDrawerItems.get(position).getUnselectedIcon());
            }
        }
        return convertView;
    }

   /* public void refreshCounter() {
//        MyPreferences myPreferences = MyPreferences.getInstance();
//        String counter = myPreferences.getNOTIFICATION_COUNTER(context);
//        navDrawerItems.get(3).setCount(counter);
        notifyDataSetChanged();
    }*/


    private class MyHolder {

        // Header
        private TextView txtUserName;
        private TextView txtMobile;
        // List Item
        private LinearLayout menuLayout;
        private ImageView imgIcon;
        private TextView txtTitle;

        public MyHolder(View convertView, int pos) {
            if (pos == 0) {
                txtUserName = (TextView) convertView.findViewById(R.id.txt_name_drawer);
                txtMobile = (TextView) convertView.findViewById(R.id.txt_mobile_drawer);
            } else {
                menuLayout = (LinearLayout) convertView.findViewById(R.id.menu_layout_item);
                imgIcon = (ImageView) convertView.findViewById(R.id.icon);
                txtTitle = (TextView) convertView.findViewById(R.id.title);
            }
        }
    }
}
