package com.loginworks.royaldines.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.loginworks.royaldines.R;


public class HomeMoreItem extends AppCompatActivity implements View.OnClickListener {

    private Toolbar mToolbar;
    private ImageView img_back_arrow;
    private LinearLayout lout_about_us, lout_contact_us, lout_support;
    private Activity mActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_more_item);
        mActivity = this;

        findViews();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        //All the id's are decribed here for all widgets
    }

    private void findViews() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        lout_about_us = (LinearLayout) findViewById(R.id.about_us);
        lout_contact_us = (LinearLayout) findViewById(R.id.contact_us);
        lout_support = (LinearLayout) findViewById(R.id.support);

        lout_about_us.setOnClickListener(this);
        lout_contact_us.setOnClickListener(this);
        lout_support.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.about_us:
                startActivity(new Intent(mActivity, AboutUS.class));
                break;
            case R.id.support:
               // Toast.makeText(mActivity, getResources().getString(R.string.comming_soon), Toast.LENGTH_SHORT).show();
                startActivity(new Intent(mActivity, Support.class));
                break;
            case R.id.contact_us:
                startActivity(new Intent(mActivity, ContactUS.class));
                break;
        }
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
}
