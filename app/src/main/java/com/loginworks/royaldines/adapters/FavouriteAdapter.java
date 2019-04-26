package com.loginworks.royaldines.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loginworks.royaldines.R;
import com.loginworks.royaldines.activity.DashboardActivity;
import com.loginworks.royaldines.databasehandler.DataHandlerDB;
import com.loginworks.royaldines.extras.MyPreferences;
import com.loginworks.royaldines.extras.ServiceURL;
import com.loginworks.royaldines.models.AddOn;
import com.loginworks.royaldines.models.Food;
import com.loginworks.royaldines.utility.CartCounter;
import com.loginworks.royaldines.utility.CartCounterIMPL;
import com.loginworks.royaldines.utility.Util;

import java.util.ArrayList;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    public static TextView tv_item_count, tv_total;
    private ArrayList<Food> favouriteArrayList;
    private Context context;
    private Activity mActivity;
    private MyPreferences myPreferences;
    private String TAG = FavouriteAdapter.class.getSimpleName();
    private DataHandlerDB dataHandlerDB;
    private AlertDialog mAlertDailog;
    private RecyclerView rv_addOn;
    private ImageView iv_addon_close;
    private LinearLayout ll_total_cart;
    private Util util;
    private RecyclerView rv_favourite;
    private LinearLayout ll_no_favourite;
    private RelativeLayout rl_fav_cart;
    private TextView fav_cart_counter;

    public FavouriteAdapter(Context context, ArrayList<Food> favouriteArrayList, RecyclerView rv_favourite,
                            LinearLayout ll_no_favourite, RelativeLayout rl_fav_cart, TextView fav_cart_counter) {
        this.context = context;
        mActivity = (Activity) context;
        this.favouriteArrayList = favouriteArrayList;

        this.fav_cart_counter = fav_cart_counter;
        this.rv_favourite = rv_favourite;
        this.ll_no_favourite = ll_no_favourite;
        this.rl_fav_cart = rl_fav_cart;

        myPreferences = MyPreferences.getInstance();
        dataHandlerDB = DataHandlerDB.getInstance();
        util = Util.getInstance();
    }

    @Override
    public FavouriteViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.favourtie_list_item, parent, false);
        return new FavouriteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final FavouriteViewHolder holder, final int position) {
        final Food favouriteFood = favouriteArrayList.get(position);
        holder.setData(favouriteFood, position);
        holder.setListeners();
    }

    @Override
    public int getItemCount() {
        return favouriteArrayList.size();
    }


    public class FavouriteViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView tv_favourite_food_name, tv_favourite_description, tv_favourite_price, tv_fav_counter;
        private ImageView iv_favourite_food, iv_favourite_food_veg, iv_favourite_food_non, iv_favourite_delete, iv_fav_minus, iv_fav_plus;
        private int position;
        private Food favouriteFood;


        public FavouriteViewHolder(View view) {
            super(view);
            tv_favourite_food_name = (TextView) view.findViewById(R.id.tv_favourite_food_name);
            tv_favourite_description = (TextView) view.findViewById(R.id.tv_favourite_description);
            tv_favourite_price = (TextView) view.findViewById(R.id.tv_favourite_price);
            tv_fav_counter = (TextView) view.findViewById(R.id.tv_food_fav_counter);

            iv_favourite_food = (ImageView) view.findViewById(R.id.iv_favourite_food);
            iv_favourite_food_veg = (ImageView) view.findViewById(R.id.iv_food_favourite_type);
            iv_favourite_food_non = (ImageView) view.findViewById(R.id.iv_food_favourite_type_non);
            iv_favourite_delete = (ImageView) view.findViewById(R.id.iv_favourite_delete);

            iv_fav_minus = (ImageView) view.findViewById(R.id.iv_fav_minus);
            iv_fav_plus = (ImageView) view.findViewById(R.id.iv_fav_plus);
        }

        public void setListeners() {
            iv_favourite_delete.setOnClickListener(FavouriteViewHolder.this);
            iv_fav_minus.setOnClickListener(FavouriteViewHolder.this);
            iv_fav_plus.setOnClickListener(FavouriteViewHolder.this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_favourite_delete:
                    dataHandlerDB.insertFavouriteData(favouriteArrayList.get(position));
                    favouriteArrayList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, favouriteArrayList.size());

                    if (favouriteArrayList.size() == 0) {
                        rv_favourite.setVisibility(View.GONE);
                        ll_no_favourite.setVisibility(View.VISIBLE);
                    }
                    Toast.makeText(context, context.getResources().getString(R.string.removed_from_favourite), Toast.LENGTH_SHORT).show();
                    break;
                case R.id.iv_fav_minus:
                    if (tv_fav_counter.getText().equals("0")) {
                        Toast.makeText(context, context.getResources().getString(R.string.cannot_be_zero), Toast.LENGTH_SHORT).show();
                        return;
                    }

                    int minusCounter = Integer.parseInt(tv_fav_counter.getText().toString());
                    minusCounter--;
                    tv_fav_counter.setText(minusCounter + "");
                    if (tv_fav_counter.getText().equals("0")) {
                        dataHandlerDB.deleteAddOnProductWise(favouriteArrayList.get(position).getProduct_id());
                    }
                    dataHandlerDB.insertCartData(favouriteArrayList.get(position),
                            tv_fav_counter.getText().toString());


                    setCartCounter();

                    break;
                case R.id.iv_fav_plus:
                    int plusCounter = Integer.parseInt(tv_fav_counter.getText().toString());
                    plusCounter++;
                    tv_fav_counter.setText(plusCounter + "");

                    dataHandlerDB.insertCartData(favouriteArrayList.get(position),
                            tv_fav_counter.getText().toString());

                    setCartCounter();

                    ArrayList<AddOn> addOnArrayList = dataHandlerDB.getManageAddon(favouriteArrayList.get(position).getProduct_id());

                    if (addOnArrayList != null && addOnArrayList.size() > 0) {

                        dataHandlerDB.insertManageAddOnData(addOnArrayList);

                        AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(context);
                        final Activity mActivity = (Activity) context;
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
                                if (mAlertDailog != null)
                                    mAlertDailog.dismiss();
                            }
                        });

                        ll_total_cart = (LinearLayout) dialogView.findViewById(R.id.ll_total_cart);
                        ll_total_cart.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if (tv_item_count.getText().toString().equalsIgnoreCase("0")) {
                                    Toast.makeText(mActivity, context.getResources().getString(R.string.no_item_into_cart), Toast.LENGTH_SHORT).show();
                                    return;
                                }
//                                util.openFragment(mActivity, FragmentsName.CHECKOUT, FragmentsName.CHECKOUT_ID, null);
                                Intent intent = new Intent(mActivity, DashboardActivity.class);
                                intent.putExtra("page", 7);
                                mActivity.startActivity(intent);
                                mAlertDailog.cancel();
                            }
                        });

                        tv_item_count = (TextView) dialogView.findViewById(R.id.tv_item_count);
                        tv_total = (TextView) dialogView.findViewById(R.id.tv_total);

                        tv_item_count.setText(dataHandlerDB.getTotalQty() + "");
                        tv_total.setText("SR " + dataHandlerDB.getTotalAmount() + " /-");

                        FavouriteAddOnAdapter addOnAdapter = new FavouriteAddOnAdapter(mActivity, addOnArrayList);
                        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(context);
                        rv_addOn.setLayoutManager(mLayoutManager);
                        rv_addOn.setAdapter(addOnAdapter);
                        if (mAlertDailog != null)
                            mAlertDailog.show();
                    }
                    break;
            }
        }

        private void setCartCounter() {
            CartCounter cartCounter = new CartCounterIMPL();
            cartCounter.setCounter(rl_fav_cart, fav_cart_counter);
        }

        public void setData(Food favouriteFood, int position) {

            if(favouriteFood.getProduct_type().equalsIgnoreCase("Non-veg")){
                this.iv_favourite_food_veg.setVisibility(View.GONE);
                this.iv_favourite_food_non.setVisibility(View.VISIBLE);
            }else{
                this.iv_favourite_food_veg.setVisibility(View.VISIBLE);
                this.iv_favourite_food_non.setVisibility(View.GONE);
            }

            this.tv_favourite_food_name.setText(favouriteFood.getProduct_name());
            this.tv_favourite_description.setText(favouriteFood.getDiscription());
            this.tv_favourite_price.setText("SR " + favouriteFood.getAmount() + " /-");
            this.favouriteFood = favouriteFood;
            this.position = position;

            int cartProductQty = dataHandlerDB.cartProductQty(favouriteFood.getProduct_id());
            this.tv_fav_counter.setText(cartProductQty + "");

            Glide.with(context)
                    .load(ServiceURL.BASE_IMAGE_PATH + ServiceURL.PRODUCT_IMAGE_PATH + favouriteFood.getProduct_img())
                    .placeholder(R.mipmap.placeholder_img)
                    .fitCenter()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .error(R.mipmap.placeholder_img)
                    .into(this.iv_favourite_food);
        }

        public int getFavouriteSize() {
            if (favouriteArrayList.size() > 0) {
                return favouriteArrayList.size();
            }
            return 0;
        }
    }

}
