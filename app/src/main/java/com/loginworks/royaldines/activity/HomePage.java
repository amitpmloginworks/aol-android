package com.loginworks.royaldines.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loginworks.royaldines.R;
import com.loginworks.royaldines.adapters.FlatDealAdapter;
import com.loginworks.royaldines.extras.Appstatus;
import com.loginworks.royaldines.extras.Const;
import com.loginworks.royaldines.extras.JsonRequestAll;
import com.loginworks.royaldines.extras.MyPreferences;
import com.loginworks.royaldines.extras.ProgressDialogUtils;
import com.loginworks.royaldines.extras.ServiceURL;
import com.loginworks.royaldines.models.FlatDeals;
import com.loginworks.royaldines.models.Home;
import com.loginworks.royaldines.models.HomeBackground;
import com.loginworks.royaldines.services.WebServiceAbstract;
import com.loginworks.royaldines.services.WebServiceClient;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class HomePage extends BaseActivity implements View.OnClickListener {

    public static final int BG_HOME_REQUEST = 2323;
    private int temp = 0;
    private Activity mActivity;
    private ImageView imv_order_now, imv_recent_order, imv_favourites, imv_more;
    private ViewPager vp_flatdeal;
    private ImageView homeImage;
    private FlatDealAdapter flatDealAdapter;
    private List<ImageView> dots;
    private LinearLayout ll_dots;
    private MyPreferences myPreferences;
    private Timer timer;
    private ArrayList<FlatDeals> flatDealsArrayList;
    private boolean doubleBack = false;
    private ViewGroup mRootView;
    private ImageView[] IMGS;
    private int[] drawable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        //Get the Views of screen
        findViews();
        mActivity = this;
        IMGS = new ImageView[]{imv_order_now, imv_recent_order, imv_favourites, imv_more};
        drawable = new int[]{R.drawable.order_now, R.drawable.recent_order, R.drawable.favourites, R.drawable.more};

        /*
        * Set drawables on ImageView widgets using Glide to maintain
        * the Cache and reduce the image size
        * */
        settingUIImages(IMGS, drawable);

        myPreferences = MyPreferences.getInstance();
        setupUI();

        if (!myPreferences.getBranchId(mActivity).equals("")) {
            if (Appstatus.getInstance(mActivity).isOnline()) {
                hitbackgroundTask(mActivity, myPreferences.getBranchId(mActivity));
            } else {
                Toast.makeText(mActivity, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
            }
        }
    }


    /*
    * finding the widhet ID's
    * */
    private void findViews() {
        mRootView = (ViewGroup) findViewById(R.id.rl_home_container);
        homeImage = (ImageView) findViewById(R.id.homeImage);
        vp_flatdeal = (ViewPager) findViewById(R.id.vp_advertisement);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);
        imv_order_now = (ImageView) findViewById(R.id.imv_order_now);
        imv_recent_order = (ImageView) findViewById(R.id.imv_recent_order);
        imv_favourites = (ImageView) findViewById(R.id.imv_favourites);
        imv_more = (ImageView) findViewById(R.id.imv_more);

        mRootView.setOnClickListener(this);
        imv_order_now.setOnClickListener(this);
        imv_recent_order.setOnClickListener(this);
        imv_favourites.setOnClickListener(this);
        imv_more.setOnClickListener(this);
    }

    private void settingUIImages(ImageView[] imv_order_now, int[] drawable) {

        for(int i = 0; i<IMGS.length;i++) {
            try {
                Glide.with(mActivity)
                        .load(drawable[i])
                        .placeholder(drawable[i])
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(drawable[i])
                        .into(imv_order_now[i]);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }

    }

    private void setupUI() {
        vp_flatdeal.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                //   if (flatDealsArrayList.size() > 1) {
                selectDot(position);
                //  }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imv_order_now:
                Intent intent = new Intent(mActivity, DashboardActivity.class);
                mActivity.startActivity(intent);
                break;
            case R.id.imv_recent_order:
                Intent intent1 = new Intent(mActivity, DashboardActivity.class);
                intent1.putExtra("page", 3);
                mActivity.startActivity(intent1);
                break;
            case R.id.imv_favourites:
                Intent favintent = new Intent(mActivity, FavouriteActivity.class);
                mActivity.startActivity(favintent);
                break;
            case R.id.imv_more:
                startActivity(new Intent(mActivity, HomeMoreItem.class));
                break;
            case R.id.rl_home_container:
                // Fade fade = new Fade();
                //  fade.setDuration(5000);
//                TransitionManager.beginDelayedTransition(mRootView, fade);
                //  TransitionManager.beginDelayedTransition(mRootView);
                //   toggleVisibility(imv_order_now, imv_recent_order, imv_favourites, imv_more);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (doubleBack) {
            finish();
            return;
        }
        this.doubleBack = true;
        Toast.makeText(this, getResources().getString(R.string.exit), Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBack = false;
            }
        }, 2000);
    }

    /*Authenticating user, based on there credentails*/
    private void hitbackgroundTask(final Activity mActivity, String branchId) {

        try {
            ProgressDialogUtils.startProgressBar(mActivity, getResources().getString(R.string.pleasewait));
            JSONObject setRequestHome = JsonRequestAll.setRequestHome(branchId);
            Log.e("Home REQUEST:::", setRequestHome.toString());

            WebServiceAbstract webServiceAbstract = new WebServiceAbstract() {
                @Override
                public void onComplete(JSONObject jsonObject) {
                    try {
                        if (jsonObject != null) {
                            ProgressDialogUtils.stopProgress();
                            afterResponse(mActivity, jsonObject);
                            Log.e("Home RESPONSE:::", jsonObject.toString());
                        } else {
                            Toast.makeText(mActivity, getResources().getString(R.string.tryagain), Toast.LENGTH_SHORT).show();
                            ProgressDialogUtils.stopProgress();
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                        ProgressDialogUtils.stopProgress();
                    }
                }
            };

            WebServiceClient.callPOSTService(ServiceURL.HOME, setRequestHome,
                    Const.JSON_OBJECT_RESPONSE, null, webServiceAbstract, Request.Method.POST);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void afterResponse(final Activity mActivity, JSONObject jsonObject) {
        try {

            final Home home = new Home(jsonObject);

            myPreferences.setAolToken(mActivity, home.getToken());
            if (home.getMinOrder() != null) {
                myPreferences.setMinOrder(mActivity, home.getMinOrder());
            }

            flatDealsArrayList = home.getFlatDealsList();
            if (flatDealsArrayList.size() > 0) {
                flatDealAdapter = new FlatDealAdapter(mActivity, flatDealsArrayList);
                vp_flatdeal.setAdapter(flatDealAdapter);
                if (flatDealsArrayList.size() > 1) {
                    createPagerDots();
                }
            } else {
                vp_flatdeal.setVisibility(View.INVISIBLE);//No Flat Deals
            }

            final ArrayList<HomeBackground> homeBackgroundArrayList = home.getHomeBgList();
            if (homeBackgroundArrayList.size() > 1 && homeBackgroundArrayList != null) {

                timer = new Timer();
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Log.e("HOME BG IMAGE :: " + temp, "::  " + ServiceURL.BASE_IMAGE_PATH + homeBackgroundArrayList.get(temp).getPath());
                                    Glide.with(mActivity)
                                            .load(ServiceURL.BASE_IMAGE_PATH + homeBackgroundArrayList.get(temp).getPath())
                                            .placeholder(R.drawable.splash_old)
                                            .fitCenter()
                                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                                            .error(R.drawable.splash_old)
                                            .into(homeImage);
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }

                                temp = temp + 1;
                                if (temp == homeBackgroundArrayList.size()) {
                                    temp = 0;
                                }
                            }
                        });
                    }
                };
                timer.schedule(timerTask, 5000, 5000);
            } else {
                if (homeBackgroundArrayList.size() > 0) {
                    Glide.with(this).load(ServiceURL.BASE_IMAGE_PATH + homeBackgroundArrayList.get(0).getPath())
                            .asBitmap()
                            .placeholder(R.drawable.splash_old)
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(homeImage);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void createPagerDots() {
        dots = new ArrayList<>();
        for (int temp = 0; temp < flatDealsArrayList.size(); temp++) {
            ImageView imageView = new ImageView(getApplicationContext());
            imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            imageView.setImageResource(R.drawable.unselected_dot);
            imageView.getLayoutParams().height = 10;
            imageView.getLayoutParams().width = 10;
            imageView.setId(temp);
            ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) imageView.getLayoutParams();
            marginParams.setMargins(0, 5, 5, 0);
            ll_dots.addView(imageView);
            dots.add(imageView);
        }
        selectDot(0);//Selecting first dot in ViewPager
    }

    private void selectDot(int idx) {
        Resources res = getResources();
        for (int i = 0; i < flatDealsArrayList.size(); i++) {
            int drawableId = (i == idx) ? (R.drawable.selected_dot) : (R.drawable.unselected_dot);
            Drawable drawable = res.getDrawable(drawableId);
            dots.get(i).setImageDrawable(drawable);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (timer != null) {
            timer.cancel();
        }
    }
}
