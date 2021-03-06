package com.loginworks.royaldines.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.loginworks.royaldines.R;
import com.loginworks.royaldines.activity.FavouriteActivity;
import com.loginworks.royaldines.databasehandler.DataHandlerDB;
import com.loginworks.royaldines.extras.MyPreferences;
import com.loginworks.royaldines.extras.ServiceURL;
import com.loginworks.royaldines.models.AddOn;
import com.loginworks.royaldines.utility.CartRefresh;
import com.loginworks.royaldines.utility.CartRefreshIMPL;

import java.util.ArrayList;

/**
 * Created by abc on 10-Apr-17.
 */

public class FavouriteAddOnAdapter extends RecyclerView.Adapter<FavouriteAddOnAdapter.AddOnViewHolder> {

    private ArrayList<AddOn> addOnArrayList;
    private Context context;
    private MyPreferences myPreferences;
    private String TAG = FavouriteAddOnAdapter.class.getSimpleName();
    private DataHandlerDB dataHandlerDB;


    public FavouriteAddOnAdapter(Context context, ArrayList<AddOn> addOnArrayList) {
        this.context = context;
        this.addOnArrayList = addOnArrayList;
        myPreferences = MyPreferences.getInstance();
        dataHandlerDB = DataHandlerDB.getInstance();
    }

    @Override
    public AddOnViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.addon_list_item, parent, false);
        return new AddOnViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AddOnViewHolder holder, final int position) {
        AddOn addOn = addOnArrayList.get(position);
        holder.tv_addon_food_name.setText(addOn.getProduct_name());
        holder.tv_addon_description.setText(addOn.getDiscription());
        holder.tv_addon_price.setText("SR " + addOn.getAmount() + " /-");


        Glide.with(context)
                .load(ServiceURL.BASE_IMAGE_PATH + ServiceURL.PRODUCT_IMAGE_PATH + addOn.getProduct_img())
                .placeholder(R.mipmap.placeholder_img)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .error(R.mipmap.placeholder_img)
                .into(holder.iv_addon_food);

        //getting qty of particular addon product
        int addOnProductQty = dataHandlerDB.getAddOnProductQty(addOn.getAddon_id());
        holder.tv_addon_food_counter.setText(addOnProductQty + "");

        holder.iv_addon_minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.tv_addon_food_counter.getText().equals("0")) {
                    Toast.makeText(context, context.getResources().getString(R.string.cannot_be_zero), Toast.LENGTH_SHORT).show();
                    return;
                }

                int crntCounter = Integer.parseInt(holder.tv_addon_food_counter.getText().toString());
                crntCounter--;
                holder.tv_addon_food_counter.setText(crntCounter + "");

                dataHandlerDB.insertAddOnData(addOnArrayList.get(position).getAddon_id(),
                        holder.tv_addon_food_counter.getText().toString(), addOnArrayList.get(position));
                refreshUI();
            }
        });

        holder.iv_addon_plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int crntCounter = Integer.parseInt(holder.tv_addon_food_counter.getText().toString());
                crntCounter++;
                holder.tv_addon_food_counter.setText(crntCounter + "");
                dataHandlerDB.insertAddOnData(addOnArrayList.get(position).getAddon_id(),
                        holder.tv_addon_food_counter.getText().toString(), addOnArrayList.get(position));
                refreshUI();
            }
        });
    }

    private void refreshUI() {
        CartRefresh cartRefresh = new CartRefreshIMPL();
        cartRefresh.setCartAmt(FavouriteAdapter.tv_total);

        cartRefresh.setCartQty(FavouriteAdapter.tv_item_count);
        cartRefresh.setCartQty(FavouriteActivity.fav_cart_counter);
    }

    @Override
    public int getItemCount() {
        return addOnArrayList.size();
    }

    public class AddOnViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_addon_food_name, tv_addon_description, tv_addon_price, tv_addon_food_counter;
        public ImageView iv_addon_food, iv_addon_plus, iv_addon_minus;

        public AddOnViewHolder(View view) {
            super(view);
            tv_addon_food_name = (TextView) view.findViewById(R.id.tv_addon_food_name);
            tv_addon_description = (TextView) view.findViewById(R.id.tv_addon_description);
            tv_addon_price = (TextView) view.findViewById(R.id.tv_addon_price);
            tv_addon_food_counter = (TextView) view.findViewById(R.id.tv_addon_food_counter);

            iv_addon_food = (ImageView) view.findViewById(R.id.iv_addon_food);
            iv_addon_plus = (ImageView) view.findViewById(R.id.iv_addon_plus);
            iv_addon_minus = (ImageView) view.findViewById(R.id.iv_addon_minus);

        }
    }
}
