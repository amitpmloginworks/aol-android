package com.loginworks.royaldines.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loginworks.royaldines.R;
import com.loginworks.royaldines.adapters.FavouriteAdapter;
import com.loginworks.royaldines.databasehandler.DataHandlerDB;
import com.loginworks.royaldines.models.Food;
import com.loginworks.royaldines.utility.CartCounter;
import com.loginworks.royaldines.utility.CartCounterIMPL;

import java.util.ArrayList;

/**
 * Created by abc on 12-Apr-17.
 */

public class FavouriteActivity extends AppCompatActivity implements View.OnClickListener {

    public static LinearLayout ll_no_favourite;
    public static TextView fav_cart_counter;
    private RecyclerView rv_favourite;
    private Activity mActivity;
    private FavouriteAdapter mFavouriteAdapter;
    private ArrayList<Food> mFavouriteFoodList;
    private DataHandlerDB dataHandlerDB;
    private Toolbar mToolbar;
    private RelativeLayout rl_fav_cart;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);
        mActivity = this;
        dataHandlerDB = DataHandlerDB.getInstance();
        findViews();
    }

    private void findViews() {
        rv_favourite = (RecyclerView) findViewById(R.id.rv_favourite);
        ll_no_favourite = (LinearLayout) findViewById(R.id.ll_no_favourite);
        mToolbar = (Toolbar) findViewById(R.id.toolbar_favourite);
        rl_fav_cart = (RelativeLayout) findViewById(R.id.ll_fav_cart);
        rl_fav_cart.setOnClickListener(this);
        fav_cart_counter = (TextView) findViewById(R.id.fav_cart_counter);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void setupView() {
        mFavouriteFoodList = dataHandlerDB.getFavouriteData();

        CartCounter cartCounter = new CartCounterIMPL();
        cartCounter.setCounter(rl_fav_cart, fav_cart_counter);

        if (mFavouriteFoodList != null && mFavouriteFoodList.size() > 0) {
            mFavouriteAdapter = new FavouriteAdapter(mActivity, mFavouriteFoodList,
                    rv_favourite, ll_no_favourite, rl_fav_cart, fav_cart_counter);
            rv_favourite.setAdapter(mFavouriteAdapter);

            LinearLayoutManager mLayoutManager = new LinearLayoutManager(mActivity);
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            rv_favourite.setLayoutManager(mLayoutManager);

            rv_favourite.setItemAnimator(new DefaultItemAnimator());
        } else {
            rv_favourite.setVisibility(View.GONE);
            ll_no_favourite.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupView();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_fav_cart:
                Intent intent = new Intent(mActivity, DashboardActivity.class);
                intent.putExtra("page", 7);
                mActivity.startActivity(intent);
                break;
        }
    }
}
