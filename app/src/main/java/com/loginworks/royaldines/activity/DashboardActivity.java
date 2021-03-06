package com.loginworks.royaldines.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loginworks.royaldines.R;
import com.loginworks.royaldines.adapters.NavDrawerListAdapter;
import com.loginworks.royaldines.databasehandler.DataHandlerDB;
import com.loginworks.royaldines.extras.FragmentsName;
import com.loginworks.royaldines.models.NavDrawerItem;
import com.loginworks.royaldines.utility.Util;

import java.util.ArrayList;

@SuppressWarnings("ALL")
public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    public static FragmentManager fragmentManager;
    public static ArrayList<NavDrawerItem> navDrawerItems;
    public static TypedArray navMenuIconsSelected, navMenuIconsUnSelected;
    public static Integer[] navMenuIcons1;
    public static String[] navMenuTitles;
    public static Toolbar toolbar;
    public ImageView imv_editOrder;
    public ImageView imv_location;
    public TextView txt_Title;
    public SearchView searchview;
    public RelativeLayout ll_cart;
    public TextView cart_counter;
    private NavDrawerListAdapter adapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    private Activity mActivity;
    private ListView mDrawerList;
    private Util util;
    private int page = 2;
    private DataHandlerDB dataHandlerDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        mActivity = DashboardActivity.this;
        dataHandlerDB = DataHandlerDB.getInstance();
        findViews();

        fragmentManager = getSupportFragmentManager();
        if (getIntent().hasExtra("page")) {
            page = getIntent().getIntExtra("page", 2);
        }

        util = Util.getInstance();

        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items); //Set the menu title
        navMenuIconsSelected = getResources().obtainTypedArray(R.array.nav_drawer_icons_selected);
        navMenuIconsUnSelected = getResources().obtainTypedArray(R.array.nav_drawer_icons_unselected);
        navMenuIcons1 = new Integer[navMenuIconsSelected.length()];
        addDrawerData();

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        displayView(page);

        imv_location.setOnClickListener(this);
        imv_editOrder.setOnClickListener(this);
        // imv_back.setOnClickListener(this);

        fragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                if (getSupportActionBar() != null) {
                    if (fragmentManager.getBackStackEntryCount() > 1) {
                        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // show back button
                        //imv_back.setVisibility(View.GONE);
                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                fragmentManager.popBackStack();
                                if (searchview != null) {
                                    searchview.onActionViewCollapsed();
                                }
                            }
                        });
                    } else {
                        //  mDrawerToggle.syncState();
//                        if (page == 7) {
//                            mDrawerToggle.setDrawerIndicatorEnabled(false);
//                            imv_back.setVisibility(View.VISIBLE);
//                        }
//                        else {
                        // mDrawerToggle.setDrawerIndicatorEnabled(true);
                        // imv_back.setVisibility(View.GONE);
                        getSupportActionBar().setDisplayHomeAsUpEnabled(false); //show hamburger
                        mDrawerToggle.syncState();

                        //  }

                        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                mDrawerLayout.openDrawer(GravityCompat.START);
                                if (searchview != null) {
                                    searchview.onActionViewCollapsed();
                                }
                            }
                        });
                    }
                }
            }
        });
    }

    private void findViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.list_slidermenu);
        navDrawerItems = new ArrayList<>();
        searchview = (SearchView) findViewById(R.id.searchview);
        imv_location = (ImageView) findViewById(R.id.imv_header_location);
        //    imv_back = (ImageView) findViewById(R.id.imv_back);
        imv_editOrder = (ImageView) findViewById(R.id.imv_header_OrderEdit);
        txt_Title = (TextView) findViewById(R.id.txt_header_title);
        cart_counter = (TextView) findViewById(R.id.cart_counter);
        ll_cart = (RelativeLayout) findViewById(R.id.ll_cart);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dataHandlerDB.getBranchCount() < 2) {
            imv_location.setVisibility(View.GONE);
        }
    }

    private void addDrawerData() {
        navDrawerItems.add(new NavDrawerItem());
        /*1*/
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[0], navMenuIconsSelected.getResourceId(0, -1), navMenuIconsUnSelected.getResourceId(0, -1)));
        /*2*/
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[1], navMenuIconsSelected.getResourceId(1, -1), navMenuIconsUnSelected.getResourceId(1, -1)));
        /*3*/
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[2], navMenuIconsSelected.getResourceId(2, -1), navMenuIconsUnSelected.getResourceId(2, -1)));
        /*4*/
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[3], navMenuIconsSelected.getResourceId(3, -1), navMenuIconsUnSelected.getResourceId(3, -1)));
        /*5*/
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[4], navMenuIconsSelected.getResourceId(4, -1), navMenuIconsUnSelected.getResourceId(4, -1)));
        /*6*/
        navDrawerItems.add(new NavDrawerItem(navMenuTitles[5], navMenuIconsSelected.getResourceId(5, -1), navMenuIconsUnSelected.getResourceId(5, -1)));
        navMenuIconsSelected.recycle();
        navMenuIconsUnSelected.recycle();

        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        adapter = new NavDrawerListAdapter(getApplicationContext(), navDrawerItems, page);
        mDrawerList.setAdapter(adapter);
//        adapter.setSelectedItem(2);

    }

    /*
    * Display view/Click event listner for different views
    * */
    private void displayView(int position) {
        switch (position) {
            case 0:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;

            case 1:
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                startActivity(new Intent(mActivity, HomePage.class));
                finishAffinity();
                break;

            case 2:
                util.openFragment(mActivity, FragmentsName.FRAGMENT_CATEGORIES, FragmentsName.CATEGORIES_ID, null);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;

            case 3:
                util.openFragment(mActivity, FragmentsName.FRAGMENT_ORDER_HISTORY, FragmentsName.ORDER_HISTORY_ID, null);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;

            case 4:
                Intent aboutUs = new Intent(mActivity, AboutUS.class);
                startActivity(aboutUs);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;

            case 5:
                Intent contactUs = new Intent(mActivity, ContactUS.class);
                startActivity(contactUs);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;

            case 6:
                Intent supportUs = new Intent(mActivity, Support.class);
                startActivity(supportUs);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;

            case 7: //From Favourite Page to Cart Page
                util.openFragment(mActivity, FragmentsName.FRAGMENT_CATEGORIES, FragmentsName.CATEGORIES_ID, null);
                util.openFragment(mActivity, FragmentsName.CHECKOUT, FragmentsName.CHECKOUT_ID, null);
                mDrawerLayout.closeDrawer(Gravity.LEFT);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imv_header_location:
                Intent intent = new Intent(mActivity, BranchSelection.class);
                startActivity(intent);
                break;

            case R.id.imv_header_OrderEdit:
                util.openFragment(mActivity, FragmentsName.CHECKOUT, FragmentsName.CHECKOUT_ID, null);
                break;

        }
    }

    @Override
    public void onBackPressed() {
        if (fragmentManager.getBackStackEntryCount() > 1) {

            fragmentManager.popBackStack();
            if (searchview != null) {
                searchview.onActionViewCollapsed();
            }
        } else {
            finish();
        }
    }

    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//            mDrawerList.clearFocus();
//            mDrawerList.post(new Runnable() {
//                @Override
//                public void run() {

            displayView(position);
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            mDrawerList.setSelected(true);
            if (position < 4)
                adapter.setSelectedItem(position);
            adapter.notifyDataSetChanged();
//                }
//            });
        }
    }

}
