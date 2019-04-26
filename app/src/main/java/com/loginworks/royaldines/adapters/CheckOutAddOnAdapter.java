package com.loginworks.royaldines.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.loginworks.royaldines.R;
import com.loginworks.royaldines.databasehandler.DataHandlerDB;
import com.loginworks.royaldines.fragments.CheckOutFragment;
import com.loginworks.royaldines.models.AddOn;
import com.loginworks.royaldines.utility.CartRefresh;
import com.loginworks.royaldines.utility.CartRefreshIMPL;

import java.util.ArrayList;

/**
 * Created by abc on 17-Apr-17.
 */

public class CheckOutAddOnAdapter extends RecyclerView.Adapter<CheckOutAddOnAdapter.AddOnHolder> {

    private DataHandlerDB dataHandlerDB;
    private ArrayList<AddOn> addonArrayList;
    private Context context;

    public CheckOutAddOnAdapter(Context context, ArrayList<AddOn> addonArrayList) {
        this.context = context;
        this.addonArrayList = addonArrayList;
        dataHandlerDB = DataHandlerDB.getInstance();
    }

    @Override
    public AddOnHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.checkout_addon_list_item, parent, false);
        return new AddOnHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AddOnHolder holder, final int position) {
        AddOn addOn = addonArrayList.get(position);
//        holder.tv_chkout_addon_food_name.setText(addOn.getProduct_name());
//        holder.tv_chkout_addon_price.setText("\u20B9" + addOn.getAmount() + " /-");
//
//        int product_qty = dataHandlerDB.getAddOnProductQty(addOn.getAddon_id());
//        holder.tv_chkout_addon_counter.setText(String.valueOf(product_qty));

        holder.setListners();
        holder.setData(position, addOn);

//        holder.iv_chkout_addon_delete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                boolean result = dataHandlerDB.deleteAddOnProduct(addonArrayList.get(position).getAddon_id());
//                if (result) {
//                    addonArrayList.remove(position);
//                    refreshUI();
//                    notifyDataSetChanged();
//                }
//            }
//        });

//        holder.iv_chkout_addon_plus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int crntCounter = Integer.parseInt(holder.tv_chkout_addon_counter.getText().toString());
//                crntCounter++;
//                holder.tv_chkout_addon_counter.setText(crntCounter + "");
//                dataHandlerDB.insertAddOnData(addonArrayList.get(position).getAddon_id(),
//                        holder.tv_chkout_addon_counter.getText().toString(), addonArrayList.get(position));
//                refreshUI();
//            }
//        });

//        holder.iv_chkout_addon_minus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int crntCounter = Integer.parseInt(holder.tv_chkout_addon_counter.getText().toString());
//                crntCounter--;
//                holder.tv_chkout_addon_counter.setText(crntCounter + "");
//
//                if (holder.tv_chkout_addon_counter.getText().toString().equalsIgnoreCase("0")) {
//                    dataHandlerDB.deleteAddOnProduct(addonArrayList.get(position).getAddon_id());
//                    addonArrayList.remove(position);
//                    refreshUI();
//                    notifyDataSetChanged();
//                    return;
//                }
//
//                dataHandlerDB.insertAddOnData(addonArrayList.get(position).getAddon_id(),
//                        holder.tv_chkout_addon_counter.getText().toString(), addonArrayList.get(position));
//                refreshUI();
//            }
//        });
    }

    private void refreshUI() {
        CartRefresh cartRefresh = new CartRefreshIMPL();
        cartRefresh.setCartAmt(CheckOutFragment.tv_check_amount);
    }

    @Override
    public int getItemCount() {
        return addonArrayList.size();
    }

    public class AddOnHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tv_chkout_addon_food_name, tv_chkout_addon_price, tv_chkout_addon_counter;
        private ImageView iv_chkout_addon_minus, iv_chkout_addon_plus, iv_chkout_addon_delete;
        private int position;

        public AddOnHolder(View itemView) {
            super(itemView);
            tv_chkout_addon_food_name = (TextView) itemView.findViewById(R.id.tv_chkout_addon_food_name);
            tv_chkout_addon_price = (TextView) itemView.findViewById(R.id.tv_chkout_addon_price);
            tv_chkout_addon_counter = (TextView) itemView.findViewById(R.id.tv_chkout_addon_counter);
            iv_chkout_addon_minus = (ImageView) itemView.findViewById(R.id.iv_chkout_addon_minus);
            iv_chkout_addon_plus = (ImageView) itemView.findViewById(R.id.iv_chkout_addon_plus);
            iv_chkout_addon_delete = (ImageView) itemView.findViewById(R.id.iv_chkout_addon_delete);
        }

        protected void setListners() {
            iv_chkout_addon_delete.setOnClickListener(AddOnHolder.this);
            iv_chkout_addon_plus.setOnClickListener(AddOnHolder.this);
            iv_chkout_addon_minus.setOnClickListener(AddOnHolder.this);
            iv_chkout_addon_delete.setOnClickListener(AddOnHolder.this);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_chkout_addon_delete:
                    boolean result = dataHandlerDB.deleteAddOnProduct(addonArrayList.get(position).getAddon_id());
                    if (result) {
                        addonArrayList.remove(position);
                        refreshUI();
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, addonArrayList.size());
                        // notifyDataSetChanged();
                    }
                    break;

                case R.id.iv_chkout_addon_plus:

                    int plusCounter = Integer.parseInt(tv_chkout_addon_counter.getText().toString());
                    plusCounter++;
                    tv_chkout_addon_counter.setText(plusCounter + "");
                    dataHandlerDB.insertAddOnData(addonArrayList.get(position).getAddon_id(),
                            tv_chkout_addon_counter.getText().toString(), addonArrayList.get(position));
                    refreshUI();

                    break;

                case R.id.iv_chkout_addon_minus:
                    int minusCounter = Integer.parseInt(tv_chkout_addon_counter.getText().toString());
                    minusCounter--;
                    tv_chkout_addon_counter.setText(minusCounter + "");

                    if (tv_chkout_addon_counter.getText().toString().equalsIgnoreCase("0")) {
                        dataHandlerDB.deleteAddOnProduct(addonArrayList.get(position).getAddon_id());
                        addonArrayList.remove(position);
                        refreshUI();
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, addonArrayList.size());
                        //notifyDataSetChanged();
                        return;
                    }

                    dataHandlerDB.insertAddOnData(addonArrayList.get(position).getAddon_id(),
                            tv_chkout_addon_counter.getText().toString(), addonArrayList.get(position));
                    refreshUI();
                    break;
            }
        }

        protected void setData(int position, AddOn addOn) {
            this.position = position;
            tv_chkout_addon_food_name.setText(addOn.getProduct_name());
            tv_chkout_addon_price.setText("SR " + addOn.getAmount() + " /-");

            int product_qty = dataHandlerDB.getAddOnProductQty(addOn.getAddon_id());
            tv_chkout_addon_counter.setText(String.valueOf(product_qty));
        }
    }
}
