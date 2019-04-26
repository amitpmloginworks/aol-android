package com.loginworks.royaldines.adapters;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.loginworks.royaldines.R;
import com.loginworks.royaldines.databasehandler.DataHandlerDB;
import com.loginworks.royaldines.extras.Appstatus;
import com.loginworks.royaldines.extras.Const;
import com.loginworks.royaldines.extras.FragmentsName;
import com.loginworks.royaldines.extras.JsonRequestAll;
import com.loginworks.royaldines.extras.MyPreferences;
import com.loginworks.royaldines.extras.ProgressDialogUtils;
import com.loginworks.royaldines.extras.ServiceURL;
import com.loginworks.royaldines.fragments.AddAddressFragment;
import com.loginworks.royaldines.models.Address;
import com.loginworks.royaldines.services.WebServiceAbstract;
import com.loginworks.royaldines.services.WebServiceClient;
import com.loginworks.royaldines.utility.Util;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ashish Verma on 4/24/2017.
 */

public class AddressAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String TAG = getClass().getSimpleName();
    private Activity mActivity;
    private ArrayList<Address> addressArrayList;
    private LayoutInflater layoutInflater;
    private Util util;
    private MyPreferences myPreferences;
    private DataHandlerDB dataHandlerDB;
    private LinearLayout ll_no_adderess;
    private RecyclerView addressRecycler;

    public AddressAdapter(Activity context, ArrayList<Address> addressArrayList, LinearLayout ll_no_adderess, RecyclerView addressRecycler) {
        this.mActivity = context;
        this.addressArrayList = addressArrayList;
        layoutInflater = LayoutInflater.from(context);
        util = Util.getInstance();
        this.ll_no_adderess = ll_no_adderess;
        this.addressRecycler = addressRecycler;
        myPreferences = MyPreferences.getInstance();
        dataHandlerDB = DataHandlerDB.getInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.delivery_address_list_item, parent, false);
        return new AddressHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        try {
            AddressHolder addressHolder = (AddressHolder) holder;
            Address address = addressArrayList.get(position);

            addressHolder.txtName.setText(address.getName());
            AddAddressFragment addAddressFragment = new AddAddressFragment();
            String add1 = null;

            Log.e("ADDRESS : 1 :", ""+address.getHouse_no());
            Log.e("ADDRESS : 2 :", ""+addAddressFragment.st_landmark);
            Log.e("ADDRESS : 3 :", ""+address.getStreet());

            if(!address.getHouse_no().equalsIgnoreCase("") && (address.getHouse_no()!=null)){
                add1 = address.getHouse_no();
            }
            if (!address.getLandmark().equalsIgnoreCase("")  && (address.getLandmark()!=null)) {
               add1 = add1 + ", " + address.getLandmark() ;
            }
            if(!address.getStreet().equalsIgnoreCase("")  && (address.getStreet()!=null)){
               add1 = add1 + ", "+ address.getStreet();
            }
            Log.e("ADDRESS : 4 :", ""+address.getDelivery_phone());

            addressHolder.txtAddress1.setText(add1.replaceAll("null,", ""));
            addressHolder.txtAddress2.setText(address.getCity());
            addressHolder.txtAddress3.setText(address.getPincode());
            addressHolder.txtMobile.setText(address.getDelivery_phone());

            addressHolder.btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putString("address_id", addressArrayList.get(position).getId());
                    myPreferences.setUserName(mActivity, addressArrayList.get(position).getName().toString());
                    util.openFragment(mActivity, FragmentsName.CONFIRM_ORDER, FragmentsName.CONFIRM_ORDER_ID, bundle);
                }
            });

            addressHolder.imgDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hitDeleteAddress(mActivity, Const.APP_ID, addressArrayList.get(position).getId(), position);
                }
            });
        }catch (NullPointerException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return addressArrayList.size();
    }

    private void hitDeleteAddress(final Activity mActivity, String appId, String addressId, final int position) {

        try {
            if (Appstatus.getInstance(mActivity).isOnline()) {
                ProgressDialogUtils.startProgressBar(mActivity, this.mActivity.getResources().getString(R.string.pleasewait));

                final JSONObject jsonObj = JsonRequestAll.createJsonParamsDeleteAddress(appId, addressId);
                Log.e(TAG + " DELETE REQUEST :: ", jsonObj + "");

                final WebServiceAbstract webServiceAbstract = new WebServiceAbstract() {
                    @Override
                    public void onComplete(JSONObject jsonObject) {
                        try {
                            if (jsonObject != null) {
                                Log.e(TAG, jsonObject.toString());
                                ProgressDialogUtils.stopProgress();
                                afterResponseOTP(mActivity, jsonObject, position);
                            } else {
                                ProgressDialogUtils.stopProgress();
                                Toast.makeText(mActivity, AddressAdapter.this.mActivity.getResources().getString(R.string.unabletoproceed), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                };

                WebServiceClient.callPOSTService(ServiceURL.DELETE_ADDRESS, jsonObj, Const.JSON_OBJECT_RESPONSE,
                        myPreferences.getAolToken(mActivity), webServiceAbstract, Request.Method.POST);

            } else {
                Toast.makeText(mActivity, this.mActivity.getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                //ProgressDialogUtils.stopProgress();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void afterResponseOTP(Activity mActivity, JSONObject jsonObject, int position) {

        try {
            if (jsonObject.getInt("status") == 1) {
                Toast.makeText(this.mActivity, this.mActivity.getResources().getString(R.string.address_deleted), Toast.LENGTH_SHORT).show();
                boolean result = dataHandlerDB.deleteAddress(addressArrayList.get(position).getId());
                addressArrayList.remove(position);
                if (addressArrayList.size() == 0) {
                    addressRecycler.setVisibility(View.GONE);
                    ll_no_adderess.setVisibility(View.VISIBLE);
                }
                notifyDataSetChanged();
            } else {
                Toast.makeText(this.mActivity, this.mActivity.getResources().getString(R.string.address_not_found), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class AddressHolder extends RecyclerView.ViewHolder {

        private TextView txtName, txtAddress1, txtAddress2, txtAddress3, txtMobile;
        private ImageView imgDelete;
        private Button btnSelect;

        public AddressHolder(View itemView) {
            super(itemView);

            txtName = (TextView) itemView.findViewById(R.id.delivery_customer_name);
            txtAddress1 = (TextView) itemView.findViewById(R.id.delivery_address);
            txtAddress2 = (TextView) itemView.findViewById(R.id.delivery_city);
            txtAddress3 = (TextView) itemView.findViewById(R.id.delivery_pincode);
            txtMobile = (TextView) itemView.findViewById(R.id.delivery_mobile);

            imgDelete = (ImageView) itemView.findViewById(R.id.delete_address);

            btnSelect = (Button) itemView.findViewById(R.id.delivery_procedd_btn);

        }
    }
}
