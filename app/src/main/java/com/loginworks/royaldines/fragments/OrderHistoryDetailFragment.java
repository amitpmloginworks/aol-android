package com.loginworks.royaldines.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.loginworks.royaldines.R;
import com.loginworks.royaldines.activity.DashboardActivity;
import com.loginworks.royaldines.adapters.OrderHistoryAdapter;
import com.loginworks.royaldines.databasehandler.DataHandlerDB;
import com.loginworks.royaldines.extras.DecimalCheck;
import com.loginworks.royaldines.extras.FragmentsName;
import com.loginworks.royaldines.extras.MyPreferences;
import com.loginworks.royaldines.models.OrderHistory;
import com.loginworks.royaldines.utility.Util;

/**
 * Created by ujjwal on 4/12/2017.
 */

public class OrderHistoryDetailFragment extends Fragment {

    private final String TAG = getClass().getSimpleName();

    private TextView txtBillerName, txtAmount, txtOrderId, txtDate, txtStatus, txtSubTotalAmount,
            txtDiscountAmount, txtTaxAmount, txtPackageCharges, txtServiceCharges, txtTotalAmount;
    private LinearLayout orderedItemLayout ,  taxLayout, packageLayout, serviceLayout;
    private RelativeLayout mainLayout;
    private Button btnReorder;
    private MyPreferences myPreferences;
    private Activity context;
    private OrderHistory orderHistory;
    private Util util;
    private DataHandlerDB dataHandlerDB;
    private TextView tvTitle;
    private TextView tvTaxs;
    private String vat = "", servicetax = "";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view1 = inflater.inflate(R.layout.fragment_order_history_detail, container, false);
        findViews(view1);
        return view1;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //postponeEnterTransition();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }
        context = getActivity();
        myPreferences = MyPreferences.getInstance();
        util = Util.getInstance();
        dataHandlerDB = DataHandlerDB.getInstance();

        orderHistory = (OrderHistory) getArguments().getSerializable("order");

        //hitBranchesTask(context, Const.APP_ID, myPreferences.getBranchId(context), orderHistory.getOrderId());

        txtBillerName.setText(orderHistory.getOrderBillName());
        txtAmount.setText(DecimalCheck.decimalFormat(Double.parseDouble(orderHistory.getOrderAmountTotal())));
        txtOrderId.setText(orderHistory.getOrderId());
        txtDate.setText(orderHistory.getOrderDate());

        for (int i = 0; i < orderHistory.getOrderedProduct().size(); i++) {
            try {
                OrderHistory.OrderedProducts orderedProducts = orderHistory.getOrderedProduct().get(i);
                View orderview = View.inflate(context, R.layout.produt_order_detail, null);
                LinearLayout productLayout = (LinearLayout) orderview.findViewById(R.id.layoutProduct);
                TextView productName = (TextView) orderview.findViewById(R.id.txtProductName);
                TextView productPrice = (TextView) orderview.findViewById(R.id.txtProductPrice);

                productName.setText((i + 1) + ". " + orderedProducts.getProductName() + " (" + orderedProducts.getProductQty() + ")");
                productPrice.setText(DecimalCheck.decimalFormat(Double.parseDouble(orderedProducts.getPrice())));
//            product.setTextColor(Color.DKGRAY);
//            product.setPadding(10, 2, 10, 2);

                double productServiceTax = 0;
                if (!orderedProducts.getSgst().equalsIgnoreCase("")) {
                    productServiceTax = Double.parseDouble(orderedProducts.getSgst());
                }

                double productVat = 0;
                if (!orderedProducts.getCgst().equalsIgnoreCase("")) {
                    productVat = Double.parseDouble(orderedProducts.getCgst());
                }


                if (productServiceTax > 0 && servicetax.length() == 0) {
                    servicetax = orderedProducts.getSgst();
                }

                if (productVat > 0 && vat.length() == 0) {
                    vat = orderedProducts.getCgst();
                }

                if (i % 2 == 0) {
                    productLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.menu_list_selected_color));
                } else {
                    productLayout.setBackgroundColor(ContextCompat.getColor(context, R.color.dark_grey));
                }

                orderedItemLayout.addView(productLayout);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        String result = "";
        if (vat.length() > 0) {
            result = "CGST " + vat + "% &";
        } else {
            result = "CGST 0.00% &";
        }
        if (servicetax.length() > 0) {
            result = result + " SGST " + servicetax + "%";
        } else {
            result = result + " SGST 0.00%";
        }

        tvTaxs.setText(result);
        try {

            txtSubTotalAmount.setText(DecimalCheck.decimalFormat(Double.parseDouble(orderHistory.getOrderActualAmount())));
            txtDiscountAmount.setText(DecimalCheck.decimalFormat(Double.parseDouble(orderHistory.getOrderDiscountAmount())));
            txtTaxAmount.setText(DecimalCheck.decimalFormat(Double.parseDouble(orderHistory.getOrderTaxAmount())));
            txtPackageCharges.setText(DecimalCheck.decimalFormat(Double.parseDouble(orderHistory.getOrderDeliveryCharge())));
            txtServiceCharges.setText(DecimalCheck.decimalFormat(Double.parseDouble(orderHistory.getOrderServiceCharge())));
            txtTotalAmount.setText(DecimalCheck.decimalFormat(Double.parseDouble(orderHistory.getOrderAmountTotal())));

            String st_taxCharge = DecimalCheck.decimalFormat(Double.parseDouble(orderHistory.getOrderTaxAmount()));
            String st_packCharge = DecimalCheck.decimalFormat(Double.parseDouble(orderHistory.getOrderDeliveryCharge()));
            String st_serviceCharge = DecimalCheck.decimalFormat(Double.parseDouble(orderHistory.getOrderServiceCharge()));

            Log.e("TAX AMOUNT ",""+st_taxCharge);
            Log.e("PACKAGE AMOUNT ",""+st_packCharge);
            Log.e("SERVICE AMOUNT ",""+st_serviceCharge);

            if(st_taxCharge.equalsIgnoreCase("0.00")){
                taxLayout.setVisibility(View.GONE);
            }
            if(st_packCharge.equalsIgnoreCase("0.00")){
                packageLayout.setVisibility(View.GONE);
            }
            if(st_serviceCharge.equalsIgnoreCase("0.00")){
                serviceLayout.setVisibility(View.GONE);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        OrderHistoryAdapter.getOrderStatus(context, orderHistory, txtStatus, btnReorder);

        btnReorder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int qty = dataHandlerDB.getTotalQty();
                if (qty > 0) {
                    AlertDialog.Builder dialog = new AlertDialog.Builder(context);
                    dialog.setCancelable(false);
                    dialog.setTitle(context.getResources().getString(R.string.confirmation_title));
                    dialog.setMessage(context.getResources().getString(R.string.reorder_confirmation));
                    dialog.setPositiveButton(context.getResources().getString(R.string.discard), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            Bundle bundle = new Bundle();
                            bundle.putBoolean("reorder", true);
                            // bundle.putString("address_id", orderHistories.get(position).getOrderAddressId());
                            dataHandlerDB.deleteAddOn();
                            dataHandlerDB.deleteAllCart();
                            dataHandlerDB.deleteManageAddOn();
                            dataHandlerDB.insertReOrderIntoCart(orderHistory.getOrderedProduct());
                            util.openFragment(context, FragmentsName.CHECKOUT, FragmentsName.CHECKOUT_ID, bundle);
                        }
                    })
                            .setNegativeButton(context.getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //Action for "Cancel".
                                }
                            });

                    final AlertDialog alert = dialog.create();
                    alert.show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("reorder", true);
                    dataHandlerDB.insertReOrderIntoCart(orderHistory.getOrderedProduct());
                    util.openFragment(context, FragmentsName.CHECKOUT, FragmentsName.CHECKOUT_ID, bundle);
                }
            }
        });


    }


    private void findViews(View view) {
        txtBillerName = (TextView) view.findViewById(R.id.txtBillerNameOrderDetail);
        txtAmount = (TextView) view.findViewById(R.id.txtOrderAmountDetail);
        txtOrderId = (TextView) view.findViewById(R.id.txtOrderIdDetail);
        txtDate = (TextView) view.findViewById(R.id.txtOrderDateDetail);
        txtStatus = (TextView) view.findViewById(R.id.txtOrderStatusDetail);
        txtSubTotalAmount = (TextView) view.findViewById(R.id.txtsubtotal_detail);
        txtDiscountAmount = (TextView) view.findViewById(R.id.discount_detail);
        txtTaxAmount = (TextView) view.findViewById(R.id.tax_detail);
        txtPackageCharges = (TextView) view.findViewById(R.id.package_detail);
        txtServiceCharges = (TextView) view.findViewById(R.id.service_charge_detail);
        txtTotalAmount = (TextView) view.findViewById(R.id.total_amount_detail);
        tvTaxs = (TextView) view.findViewById(R.id.tvTaxs);

        btnReorder = (Button) view.findViewById(R.id.btnReorderDetail);
        btnReorder.setVisibility(View.INVISIBLE);

        mainLayout = (RelativeLayout) view.findViewById(R.id.orderHistoryDetailLayout);
        orderedItemLayout = (LinearLayout) view.findViewById(R.id.orderedItemLayout);

        taxLayout = (LinearLayout) view.findViewById(R.id.tax_linear);
        packageLayout = (LinearLayout) view.findViewById(R.id.package_linear);
        serviceLayout = (LinearLayout) view.findViewById(R.id.service_linear);
    }

    @Override
    public void onResume() {
        super.onResume();

        DashboardActivity.toolbar.setVisibility(View.VISIBLE);
        tvTitle = ((DashboardActivity) context).txt_Title;
        tvTitle.setText(getResources().getString(R.string.order_history));
    }


}
