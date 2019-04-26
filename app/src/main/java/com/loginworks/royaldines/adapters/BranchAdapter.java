package com.loginworks.royaldines.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loginworks.royaldines.R;
import com.loginworks.royaldines.activity.HomePage;
import com.loginworks.royaldines.databasehandler.DataHandlerDB;
import com.loginworks.royaldines.extras.AppOnLeaseApplication;
import com.loginworks.royaldines.extras.MyPreferences;
import com.loginworks.royaldines.models.BranchParser;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * Created by Ashish Verma on 4/4/2017.
 */

public class BranchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private ArrayList<BranchParser> listBranches;
    private LayoutInflater inflater;
    private DataHandlerDB dataHandlerDB;
//    private int height, width;

    private MyPreferences myPreferences;

    public BranchAdapter(Context context, ArrayList<BranchParser> listBranches) {
        this.context = context;
        this.listBranches = listBranches;
        inflater = LayoutInflater.from(context);

        myPreferences = MyPreferences.getInstance();
        dataHandlerDB = DataHandlerDB.getInstance();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.branch_list_item, parent, false);
        CategoriesHolder holder = new CategoriesHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        CategoriesHolder categoriesHolder = (CategoriesHolder) holder;

        final BranchParser branchParser = listBranches.get(position);

        categoriesHolder.txtAddress.setText(branchParser.getLocation_name() + "," + branchParser.getAddress() + ","
                + branchParser.getCity() + "," + branchParser.getState() + "," + branchParser.getZipcode());

        float distance = calculateDistance(branchParser.getAddress(), branchParser.getLatitude(), branchParser.getLongitude());
        categoriesHolder.txtDistance.setText("" + distance);


        categoriesHolder.mLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myPreferences.setBranchId(context, listBranches.get(position).getBranch_id().trim());
                myPreferences.setBranchName(context, listBranches.get(position).getLocation_name().trim());
                myPreferences.setBranchAddress(context, listBranches.get(position).getAddress().trim());
                myPreferences.setBranchCity(context, listBranches.get(position).getCity().trim());
                myPreferences.setBranchState(context, listBranches.get(position).getState().trim());
                myPreferences.setBranchCountry(context, listBranches.get(position).getCountry().trim());
                myPreferences.setBranchZipcode(context, listBranches.get(position).getZipcode().trim());
                myPreferences.setBranchMobile(context, listBranches.get(position).getMobile().trim());
                myPreferences.setBranchPhone(context, listBranches.get(position).getPhone().trim());
                myPreferences.setBranchLatitude(context, listBranches.get(position).getLatitude().trim());
                myPreferences.setBranchLongitude(context, listBranches.get(position).getLongitude().trim());
                myPreferences.setBranchEmail(context, listBranches.get(position).getEmail().trim());
                Intent intent = new Intent(context, HomePage.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//
                ((Activity) context).setResult(HomePage.BG_HOME_REQUEST, intent);
                //TODO Notify user that all information regarding order will be removed, ask to continue/ cancel
                dataHandlerDB.deleteAllCart();
                dataHandlerDB.deleteAddOn();
                ((Activity) context).finish();
                context.startActivity(intent);
            }
        });

    }


    private float calculateDistance(String strAddress, String lat, String lon) {

        try {

            Location currentLoc = new Location("Current");
            String currentLat = myPreferences.getLatitude(AppOnLeaseApplication.getApplicationCtx());
            String currentLon = myPreferences.getLongtitude(AppOnLeaseApplication.getApplicationCtx());
            if (!currentLat.equalsIgnoreCase("") && !currentLon.equalsIgnoreCase("")) {
                currentLoc.setLatitude(Double.parseDouble(currentLat));
                currentLoc.setLongitude(Double.parseDouble(currentLon));
            }
            Location branchLoc = new Location("Branch");
            if (lat != null && lon != null && !lat.equalsIgnoreCase("") && !lon.equalsIgnoreCase("")) {
                branchLoc.setLatitude(Double.parseDouble(lat));
                branchLoc.setLongitude(Double.parseDouble(lon));
            }

            DecimalFormat decimalFormat = new DecimalFormat("#.#");
            double distance = currentLoc.distanceTo(branchLoc);

            return Float.parseFloat(decimalFormat.format(distance / 1000));

        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }


    }


    @Override
    public int getItemCount() {
        return listBranches.size();
    }

    public class CategoriesHolder extends RecyclerView.ViewHolder {

        private LinearLayout mLayout;
        private TextView txtAddress, txtDistance;

        public CategoriesHolder(View itemView) {
            super(itemView);
            mLayout = (LinearLayout) itemView.findViewById(R.id.branchListLayout);
            txtAddress = (TextView) itemView.findViewById(R.id.txtAddress);
            txtDistance = (TextView) itemView.findViewById(R.id.txtDistance);
        }
    }

}
