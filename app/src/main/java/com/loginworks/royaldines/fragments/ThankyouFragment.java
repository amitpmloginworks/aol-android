package com.loginworks.royaldines.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loginworks.royaldines.R;
import com.loginworks.royaldines.activity.DashboardActivity;
import com.loginworks.royaldines.databasehandler.DataHandlerDB;
import com.loginworks.royaldines.extras.Const;
import com.loginworks.royaldines.extras.FragmentsName;
import com.loginworks.royaldines.extras.MyPreferences;
import com.loginworks.royaldines.utility.Util;

/**
 * Created by ujjwal on 4/24/2017.
 */

public class ThankyouFragment extends Fragment {

    private TextView txtOrderId, thank_text;
    private Button btnOrderMore;
    private Activity mActivity;
    private MyPreferences myPreferences;
    private String mobile;
    private ImageView img_logo;
    Util util;
    private int drawable;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_thank_you, container, false);

        mActivity = getActivity();
        util = Util.getInstance();
        myPreferences = MyPreferences.getInstance();
        Bundle bundle = getArguments();
        drawable = R.drawable.logo_royal;

        txtOrderId = (TextView) view.findViewById(R.id.txtOrderId);
        thank_text = (TextView) view.findViewById(R.id.thank_text_b);
        btnOrderMore = (Button) view.findViewById(R.id.btnOrderMore);
        img_logo = (ImageView) view.findViewById(R.id.logo);

        settingUIImages(img_logo, drawable);
        txtOrderId.setText("Your Order Id is " + bundle.getString("order_id"));

        /*if(util.isDelivery() && !myPreferences.getDELIVERY_MOBILE(mActivity).equalsIgnoreCase("") ) {
            mobile = myPreferences.getDELIVERY_MOBILE(mActivity);
            thank_text.setText(myPreferences.getDELIVERY_MOBILE(mActivity));
        }
        else  if(util.isDelivery() && myPreferences.getDELIVERY_MOBILE(mActivity).equalsIgnoreCase("") ) {
            mobile = myPreferences.getUserMobile(mActivity);
            thank_text.setText(myPreferences.getUserMobile(mActivity));
        }
        else{
            mobile = myPreferences.getUserMobile(mActivity);
            thank_text.setText(myPreferences.getUserMobile(mActivity));

        }*/
        thank_text.setText(Const.THANK_U_NUMBER);
        thank_text.setPaintFlags(thank_text.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG); //to underline the text!

        btnOrderMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Util.getInstance().openFragment(getActivity(), FragmentsName.FRAGMENT_CATEGORIES, FragmentsName.CATEGORIES_ID, null);
            }
        });

        thank_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" +Const.THANK_U_NUMBER));
                    mActivity.startActivity(intent);
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });



        DataHandlerDB.getInstance().clearAllTables();

        return view;
    }

    private void settingUIImages(ImageView imv_order_now, int drawable) {

            try {
                Glide.with(mActivity)
                        .load(drawable)
                        .placeholder(drawable)
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(drawable)
                        .into(imv_order_now);
            } catch (Exception ex) {
                ex.printStackTrace();
            }

    }


    @Override
    public void onResume() {
        super.onResume();
        DashboardActivity.toolbar.setVisibility(View.GONE);
    }
}
