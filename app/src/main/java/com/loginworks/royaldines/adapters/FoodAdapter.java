package com.loginworks.royaldines.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loginworks.royaldines.R;
import com.loginworks.royaldines.databasehandler.DataHandlerDB;
import com.loginworks.royaldines.extras.FragmentsName;
import com.loginworks.royaldines.extras.ServiceURL;
import com.loginworks.royaldines.fragments.FoodFragment;
import com.loginworks.royaldines.models.AddOn;
import com.loginworks.royaldines.models.Food;
import com.loginworks.royaldines.utility.CartRefresh;
import com.loginworks.royaldines.utility.CartRefreshIMPL;
import com.loginworks.royaldines.utility.Util;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by abc on 10-Apr-17.
 */

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    public static TextView tv_item_count, tv_total;
    private ArrayList<Food> foodArrayList;
    private Context mContext;
    private AlertDialog mAlertDailog;
    private RecyclerView rv_addOn;
    private ImageView iv_addon_close;
    private LinearLayout ll_total_cart;
    private DataHandlerDB dataHandlerDB;
    private HashMap<String, ArrayList<AddOn>> addonHashMap;
    private Util util;
    private int height, width;

    public FoodAdapter(Context context, ArrayList<Food> foodArrayList, HashMap<String, ArrayList<AddOn>> addonHashMap) {
        this.mContext = context;
        util = Util.getInstance();
        this.foodArrayList = foodArrayList;
        this.addonHashMap = addonHashMap;
        dataHandlerDB = DataHandlerDB.getInstance();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        width = displayMetrics.widthPixels;
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_list_item, parent, false);
        return new FoodViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FoodViewHolder holder, final int position) {
        final Food food = foodArrayList.get(position);

        holder.tv_food_name.setText(food.getProduct_name());
        holder.tv_description.setText(food.getDiscription());
        holder.tv_price.setText("SR " + food.getAmount() + " /-");

        if(food.getProduct_type().equalsIgnoreCase("Non-veg")){
            holder.iv_food_type.setVisibility(View.GONE);
            holder.iv_food_type_non.setVisibility(View.VISIBLE);
        }else{
            holder.iv_food_type.setVisibility(View.VISIBLE);
            holder.iv_food_type_non.setVisibility(View.GONE);
        }

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, width * 1 / 2);
        holder.iv_food.setLayoutParams(params);

        Glide.with(mContext)
                .load(ServiceURL.BASE_IMAGE_PATH + ServiceURL.PRODUCT_IMAGE_PATH + food.getProduct_img())
                .placeholder(R.mipmap.placeholder_img)
                .fitCenter()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.placeholder_img)
                .into(holder.iv_food);

        //Checking product is favourite or not
        boolean isFavourite = dataHandlerDB.isFavourite(food.getProduct_id());
        if (isFavourite) {
            holder.iv_favourite.setImageDrawable(mContext.getResources().getDrawable(R.drawable.like));
        } else {
            holder.iv_favourite.setImageDrawable(mContext.getResources().getDrawable(R.drawable.unlike));
        }

        //getting qty of particular product
        int cartProductQty = dataHandlerDB.cartProductQty(food.getProduct_id());
        holder.tv_food_counter.setText(cartProductQty + "");

        holder.iv_favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String result = dataHandlerDB.insertFavouriteData(foodArrayList.get(position));
                if (result.equalsIgnoreCase("inserted")) {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.added_to_favourite), Toast.LENGTH_SHORT).show();
                    holder.iv_favourite.setImageDrawable(mContext.getResources().getDrawable(R.drawable.like));

                    String product_id = foodArrayList.get(position).getProduct_id();
                    ArrayList<AddOn> addOnArrayList = addonHashMap.get(product_id);

                    if (addOnArrayList != null && addOnArrayList.size() > 0) {
                        dataHandlerDB.insertManageAddOnData(addOnArrayList);
                    }

                } else {
                    Toast.makeText(mContext, mContext.getResources().getString(R.string.removed_from_favourite), Toast.LENGTH_SHORT).show();
                    holder.iv_favourite.setImageDrawable(mContext.getResources().getDrawable(R.drawable.unlike));
                }
            }
        });


        holder.iv_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    if (holder.tv_food_counter.getText().equals("0")) {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.cannot_be_zero), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int crntCounter = Integer.parseInt(holder.tv_food_counter.getText().toString());
                    crntCounter--;
                    holder.tv_food_counter.setText(crntCounter + "");
                    if (holder.tv_food_counter.getText().equals("0")) {
                        dataHandlerDB.deleteAddOnProductWise(foodArrayList.get(position).getProduct_id());
                    }
                    dataHandlerDB.insertCartData(foodArrayList.get(position),
                            holder.tv_food_counter.getText().toString());

                    refreshUI();
                }catch (NumberFormatException e){
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

        holder.iv_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try{
                String product_id = foodArrayList.get(position).getProduct_id();
                ArrayList<AddOn> addOnArrayList = addonHashMap.get(product_id);

                if (addOnArrayList != null && addOnArrayList.size() > 0) {

                    if (mAlertDailog == null) {

                        int crntCounter = Integer.parseInt(holder.tv_food_counter.getText().toString());
                        crntCounter++;
                        holder.tv_food_counter.setText(crntCounter + "");

                        dataHandlerDB.insertCartData(foodArrayList.get(position),
                                holder.tv_food_counter.getText().toString());

                        refreshUI();

                        dataHandlerDB.insertManageAddOnData(addOnArrayList);

                        final AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(mContext);
                        final Activity mActivity = (Activity) mContext;
                        LayoutInflater inflater = mActivity.getLayoutInflater();
                        View dialogView = inflater.inflate(R.layout.addon_dialog, null);
                        mAlertBuilder.setView(dialogView);

                        mAlertBuilder.setCancelable(true);
                        mAlertDailog = mAlertBuilder.create();
                        rv_addOn = (RecyclerView) dialogView.findViewById(R.id.rv_addon_food);
                        iv_addon_close = (ImageView) dialogView.findViewById(R.id.addon_close);
                        iv_addon_close.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (mAlertDailog != null) {
                                    mAlertDailog.dismiss();
                                }
                            }
                        });

                        ll_total_cart = (LinearLayout) dialogView.findViewById(R.id.ll_total_cart);
                        ll_total_cart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (tv_item_count.getText().toString().equalsIgnoreCase("0")) {
                                    Toast.makeText(mActivity, mContext.getResources().getString(R.string.no_item_into_cart), Toast.LENGTH_SHORT).show();
                                    return;
                                }
                                util.openFragment(mActivity, FragmentsName.CHECKOUT, FragmentsName.CHECKOUT_ID, null);
                                mAlertDailog.cancel();
                            }
                        });

                        tv_item_count = (TextView) dialogView.findViewById(R.id.tv_item_count);
                        tv_total = (TextView) dialogView.findViewById(R.id.tv_total);

                        tv_item_count.setText(dataHandlerDB.getTotalQty() + "");
                        tv_total.setText("SR " + dataHandlerDB.getTotalAmount() + " /-");

                        AddOnAdapter addOnAdapter = new AddOnAdapter(mActivity, addOnArrayList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                        rv_addOn.setLayoutManager(mLayoutManager);
                        rv_addOn.setAdapter(addOnAdapter);

                        mAlertDailog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                mAlertDailog = null;
                            }
                        });

                        if (mAlertDailog != null && !mAlertDailog.isShowing()) {
                            mAlertDailog.show();
                        }

                    }
                } else {

                    int crntCounter = Integer.parseInt(holder.tv_food_counter.getText().toString());
                    crntCounter++;
                    holder.tv_food_counter.setText(crntCounter + "");

                    dataHandlerDB.insertCartData(foodArrayList.get(position),
                            holder.tv_food_counter.getText().toString());

                    refreshUI();
                }

                }catch (NumberFormatException e){
                    e.printStackTrace();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        });


    }

    private void refreshUI() {
        CartRefresh cartRefresh = new CartRefreshIMPL();
        cartRefresh.setCartAmt(FoodFragment.tv_total_amount);
        cartRefresh.setCartQty(FoodFragment.tv_item_count);
    }

    @Override
    public int getItemCount() {
        return foodArrayList.size();
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_food_name, tv_description, tv_price, tv_food_counter;
        private ImageView iv_food, iv_food_type, iv_food_type_non, iv_favourite, iv_plus, iv_minus;


        public FoodViewHolder(View view) {
            super(view);
            tv_food_name = (TextView) view.findViewById(R.id.tv_food_name);
            tv_description = (TextView) view.findViewById(R.id.tv_description);
            tv_price = (TextView) view.findViewById(R.id.tv_price);
            tv_food_counter = (TextView) view.findViewById(R.id.tv_food_counter);

            iv_food = (ImageView) view.findViewById(R.id.iv_food);
            iv_food_type = (ImageView) view.findViewById(R.id.iv_food_type);
            iv_food_type_non = (ImageView) view.findViewById(R.id.iv_food_type_non);

            iv_favourite = (ImageView) view.findViewById(R.id.iv_favourite);
            iv_plus = (ImageView) view.findViewById(R.id.iv_plus);
            iv_minus = (ImageView) view.findViewById(R.id.iv_minus);

        }
    }
}
