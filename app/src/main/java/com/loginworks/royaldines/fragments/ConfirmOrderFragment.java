package com.loginworks.royaldines.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.loginworks.royaldines.R;
import com.loginworks.royaldines.activity.DashboardActivity;
import com.loginworks.royaldines.databasehandler.DataHandlerDB;
import com.loginworks.royaldines.extras.Appstatus;
import com.loginworks.royaldines.extras.Const;
import com.loginworks.royaldines.extras.DecimalCheck;
import com.loginworks.royaldines.extras.FragmentsName;
import com.loginworks.royaldines.extras.JsonRequestAll;
import com.loginworks.royaldines.extras.MyPreferences;
import com.loginworks.royaldines.extras.ProgressDialogUtils;
import com.loginworks.royaldines.extras.ServiceURL;
import com.loginworks.royaldines.models.AddOn;
import com.loginworks.royaldines.models.CheckoutDeals;
import com.loginworks.royaldines.models.Food;
import com.loginworks.royaldines.models.Paytm;
import com.loginworks.royaldines.services.WebServiceAbstract;
import com.loginworks.royaldines.services.WebServiceClient;
import com.loginworks.royaldines.utility.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.regex.Pattern;

/**
 * Created by Ashish Verma on 4/24/2017.
 */

public class ConfirmOrderFragment extends Fragment implements View.OnClickListener {

    String paytmorderid = "";
    private String TAG = getClass().getSimpleName();
    private Activity mActivity;
    private MyPreferences myPreferences;
    private Util util;
    private LinearLayout productsORLayout , tax_linear, pacakage_linear, service_linear;
    private TextView txtSubTotalAMount, txtDiscountAmount, txtTotalAMount, txtTaxAmount, txtPackigAmount,
            txtServiceCharge, txtInstructions, txtTaxValues,txtsubtotaltax;
    private EditText editPromocode;
    private Button btnAplyPromocode, btnCancelOrder, btnPlaceOrder;
    private ArrayList<CheckoutDeals> checkoutDealsArrayList;
    private ArrayList<String> coupanCodeList;
    private Bundle bundle;
    private DataHandlerDB dataHandlerDB;
    private ArrayList<Food> productList;
    private ArrayList<AddOn> addOnList;
    private boolean reOrder = false;
    private String subTotalAMount, discountAmount, taxAmount, packingAmount, serviceChargeAmount,
            totalAmount, couponCode, branchId, phone, addressId, totalItemCount, instructions,
            name, orderList, imei, orderType, paymentMode, res, deliveryCharge, serviceCHarge;
    private String totalAmounttmp;
    private  double vattaxAmount;
    private double discAmount = 0;
    private double vatpercentage = 5;
    private TextView tvTitle;
    //    private SearchView searchView;
    private ImageView iv_location, imgEditPromoCode /*,imgEditInst*/;
    private ImageView imv_editOrder;
    private RelativeLayout ll_Cart;
    private LinearLayout ll_confirmButtons, ll_instructions;
    private AlertDialog alert;
    private Pattern character_Pattern = Pattern.compile("[a-zA-Z0-9.\n ]+");
    private ScrollView mParentScroll, mChildScroll;
    private WebView mPaymentWebView;
    //private String appliedCategoryCoupan;
    private HashMap<String, Double> appliedDealsMap = new HashMap<>();
    private boolean paytmEnabled = false;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_confirm_order, container, false);

        mActivity = getActivity();
        util = Util.getInstance();
        myPreferences = MyPreferences.getInstance();
        dataHandlerDB = DataHandlerDB.getInstance();

        findViews(view);


        reOrder = myPreferences.isReorder(mActivity);

//        if (reOrder) {
//            productList = dataHandlerDB.getReorderCartData();
//            addOnList = dataHandlerDB.getReorderAddOnData();
//            subTotalAMount = getSubTotalAmount(DecimalCheck.decimalFormat(dataHandlerDB.getReorderTotalAmount()));
//        } else {
        productList = dataHandlerDB.getCartData();
        addOnList = dataHandlerDB.getAddOnData();
        subTotalAMount = getSubTotalAmount(DecimalCheck.decimalFormat(dataHandlerDB.getTotalAmount()));

//        }

        taxAmount = getTaxAmount();
        vattaxAmount=Double.parseDouble(subTotalAMount)*vatpercentage/100;
        packingAmount = getPackingCharge(myPreferences.getPackingCharge(mActivity));
        serviceCHarge = getServiceCharge(myPreferences.getServiceCharge(mActivity));

        if(taxAmount.equalsIgnoreCase("0.00")){
            tax_linear.setVisibility(View.GONE);
        }
        if(packingAmount.equalsIgnoreCase("0.00")){
            pacakage_linear.setVisibility(View.GONE);
        }
        if(serviceCHarge.equalsIgnoreCase("0.00")){
            service_linear.setVisibility(View.GONE);
        }
        totalAmount = getTotalAMount(subTotalAMount, taxAmount, packingAmount, serviceCHarge, 0);
        Log.e("TOTAL AMOUNT ::", ""+totalAmount);

        bundle = getArguments();
        if (bundle != null && bundle.containsKey("address_id")) {
            addressId = bundle.getString("address_id");
        } else {
            addressId = "";
        }

        setProductList();

        txtSubTotalAMount.setText(subTotalAMount);
        txtDiscountAmount.setText(DecimalCheck.decimalFormat(0));
        txtTaxAmount.setText(taxAmount);
        txtsubtotaltax.setText(String.valueOf(vattaxAmount));
        txtPackigAmount.setText(packingAmount);
        txtServiceCharge.setText(serviceCHarge);
        //Log.d("total amount",""+Double.parseDouble(totalAmount)+vattaxAmount);
        totalAmounttmp=String.valueOf(Double.parseDouble(totalAmount)+vattaxAmount);
        txtTotalAMount.setText(totalAmounttmp);
        hitCheckoutDesls(mActivity, myPreferences.getBranchId(mActivity));

        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        editPromocode.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (keyboardShown(editPromocode.getRootView())) {
                    Log.e("keyboard", "keyboard UP");
                    ll_confirmButtons.setVisibility(View.GONE);
                } else {
                    Log.e("keyboard", "keyboard Down");
                    ll_confirmButtons.setVisibility(View.VISIBLE);
                }
            }
        });

        return view;
    }


    private void findViews(View view) {

        productsORLayout = (LinearLayout) view.findViewById(R.id.productsORLayout);
        ll_confirmButtons = (LinearLayout) view.findViewById(R.id.ll_confirm);
        ll_instructions = (LinearLayout) view.findViewById(R.id.ll_instructions);

        tax_linear = (LinearLayout) view.findViewById(R.id.tax_linear);
        pacakage_linear = (LinearLayout) view.findViewById(R.id.package_linear);
        service_linear = (LinearLayout) view.findViewById(R.id.service_linear);


        txtSubTotalAMount = (TextView) view.findViewById(R.id.txtsubtotalOrderReview);

        txtsubtotaltax= (TextView) view.findViewById(R.id.txtsubtotaltax);

        txtDiscountAmount = (TextView) view.findViewById(R.id.discountOrderReview);
        txtTaxAmount = (TextView) view.findViewById(R.id.taxOrderReview);
        txtPackigAmount = (TextView) view.findViewById(R.id.packageOrderReview);
        txtServiceCharge = (TextView) view.findViewById(R.id.service_chargeOrderReview);
        txtTotalAMount = (TextView) view.findViewById(R.id.total_amountOrderReview);
        txtInstructions = (TextView) view.findViewById(R.id.txtInstructions);
        txtTaxValues = (TextView) view.findViewById(R.id.txtTaxValues);
        // txtPromoCode = (TextView) view.findViewById(R.id.txtPromoCode);

        editPromocode = (EditText) view.findViewById(R.id.editPromoCode);
        btnAplyPromocode = (Button) view.findViewById(R.id.btnApplyPromo);
        btnCancelOrder = (Button) view.findViewById(R.id.btnCancelOrder);
        btnPlaceOrder = (Button) view.findViewById(R.id.btnConfirmOrder);

//        imgEditInst = (ImageView) view.findViewById(R.id.imgEditInstructions);
        imgEditPromoCode = (ImageView) view.findViewById(R.id.imgEditPromoCode);

        mPaymentWebView = (WebView) view.findViewById(R.id.payment_webview);

        mParentScroll = (ScrollView) view.findViewById(R.id.scroll_parent);
        mChildScroll = (ScrollView) view.findViewById(R.id.scroll_child);

        mParentScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                mChildScroll.getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            }
        });

        mChildScroll.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                // Disallow the touch request for parent scroll on touch of child view
                view.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        btnAplyPromocode.setOnClickListener(this);
        btnCancelOrder.setOnClickListener(this);
        btnPlaceOrder.setOnClickListener(this);
        ll_instructions.setOnClickListener(this);
        //imgEditInst.setOnClickListener(this);
        imgEditPromoCode.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();

        DashboardActivity.toolbar.setVisibility(View.VISIBLE);

        tvTitle = ((DashboardActivity) mActivity).txt_Title;
//        searchView = ((DashboardActivity) mActivity).searchview;
        iv_location = ((DashboardActivity) mActivity).imv_location;
        imv_editOrder = ((DashboardActivity) mActivity).imv_editOrder;

        ll_Cart = ((DashboardActivity) mActivity).ll_cart;
        ll_Cart.setVisibility(View.GONE);
        tvTitle.setText(getResources().getString(R.string.order_review));

        imv_editOrder.setVisibility(View.VISIBLE);
    }

    private void setProductList() {
        for (int i = 0; i < productList.size(); i++) {
            try{
                Food food = productList.get(i);
                View view = View.inflate(mActivity, R.layout.produt_order_review, null);
                LinearLayout productLayout = (LinearLayout) view.findViewById(R.id.layoutProduct);
                TextView productName = (TextView) view.findViewById(R.id.txtProductName);
                TextView productPrice = (TextView) view.findViewById(R.id.txtProductPrice);

                productName.setText((productsORLayout.getChildCount() + 1) + ". " + food.getProduct_name() + " (" + food.getProductQty() + ")");
                double totalprize = Double.parseDouble(food.getAmount()) * Double.parseDouble(food.getProductQty());
                //productPrice.setText(DecimalCheck.decimalFormat(Double.parseDouble(food.getAmount())));
                productPrice.setText(DecimalCheck.decimalFormat(totalprize));

                if (productsORLayout.getChildCount() % 2 == 0) {
                    productLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.menu_list_selected_color));
                } else {
                    productLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.dark_grey));
                }

                productsORLayout.addView(productLayout);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        for (int i = 0; i < addOnList.size(); i++) {
            try {
                AddOn food = addOnList.get(i);
                View view = View.inflate(mActivity, R.layout.produt_order_review, null);
                LinearLayout productLayout = (LinearLayout) view.findViewById(R.id.layoutProduct);
                TextView productName = (TextView) view.findViewById(R.id.txtProductName);
                TextView productPrice = (TextView) view.findViewById(R.id.txtProductPrice);

                productName.setText((productsORLayout.getChildCount() + 1) + ". " + food.getProduct_name() + " (" + food.getProduct_qty() + ")");
                //   productPrice.setText(DecimalCheck.decimalFormat(Double.parseDouble(food.getAmount())));
                try {
                    double totalprize = Double.parseDouble(food.getAmount()) * Double.parseDouble(food.getProduct_qty());
                    productPrice.setText(DecimalCheck.decimalFormat(totalprize));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (productsORLayout.getChildCount() % 2 == 0) {
                    productLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.menu_list_selected_color));
                } else {
                    productLayout.setBackgroundColor(ContextCompat.getColor(mActivity, R.color.dark_grey));
                }

                productsORLayout.addView(productLayout);
            }catch (NumberFormatException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }

    private String getTaxAmount() {
        double serviceTax = 0;
        double vatTax = 0;
        double swachchhBharatTax = 0;
        String result;

        if (myPreferences.isTaxStatus(mActivity)) {
            serviceTax = Double.parseDouble(getServiceTaxAmount());
            vatTax = Double.parseDouble(getVatAmount());

            swachchhBharatTax = Double.parseDouble(getSwachBharatTaxAmount());
            Log.e("CGST ", ""+myPreferences.getCGST_TaxValue(mActivity));
            Log.e("SGST ", ""+myPreferences.getSGST_TaxValue(mActivity));
                if (vatTax > 0) {
                    result = "CGST " + myPreferences.getCGST_TaxValue(mActivity) + "% & ";
                } else {
                    result = "CGST 0.00% & ";
                }
                if (serviceTax > 0) {
                    result = result + "SGST " + myPreferences.getSGST_TaxValue(mActivity) + "% ";
                } else {
                    result = result + "SGST 0.00% ";
                }
                txtTaxValues.setText(result);

        }
        return DecimalCheck.decimalFormat(serviceTax + vatTax + swachchhBharatTax);

    }

    private String getServiceTaxAmount() {
        double amount = 0;

        if (myPreferences.isTaxStatus(mActivity)) {
            for (int i = 0; i < productList.size(); i++) {
                Food food = productList.get(i);
                amount = amount + getTaxValue(food.getAmount(), food.getSgst_tax(), food.getProductQty());
                Log.e("PROD TAX AMOUNT", "" + amount);
                for (int j = 0; j < addOnList.size(); j++) {
                    AddOn addOn = addOnList.get(j);
                    if (food.getProduct_id().equals(addOn.getMain_product_id())) {
                        amount = amount + getTaxValue(addOn.getAmount(), food.getSgst_tax(), addOn.getProduct_qty());
                        Log.e("ADDON TAX AMOUNT", "" + amount);
                    }
                }
            }

            //    txtTaxValues.setText("Tax(ST " + myPreferences.getSGST_TaxValue(mActivity) + "% / VAT " + myPreferences.getCGST_TaxValue(mActivity) + "%)");
        }
        return DecimalCheck.decimalFormat(amount);
    }

    private String getSwachBharatTaxAmount() {
        double amount = 0;

        if (myPreferences.isTaxStatus(mActivity)) {
            for (int i = 0; i < productList.size(); i++) {
                Food food = productList.get(i);
                amount = amount + getTaxValue(food.getAmount(), food.getSwach_bharat_tax(), food.getProductQty());
                Log.e("PROD TAX AMOUNT", "" + amount);

                for (int j = 0; j < addOnList.size(); j++) {
                    AddOn addOn = addOnList.get(j);
                    if (food.getProduct_id().equals(addOn.getMain_product_id())) {
                        amount = amount + getTaxValue(addOn.getAmount(), food.getSwach_bharat_tax(), addOn.getProduct_qty());
                        Log.e("ADDON TAX AMOUNT", "" + amount);
                    }
                }
            }
            // txtTaxValues.setText("Tax(ST " + myPreferences.getSGST_TaxValue(mActivity) + "% / VAT " + myPreferences.getCGST_TaxValue(mActivity) + "%)");
        }
        return DecimalCheck.decimalFormat(amount);
    }

    private String getVatAmount() {
        double amount = 0;

        if (myPreferences.isTaxStatus(mActivity)) {
            for (int i = 0; i < productList.size(); i++) {
                Food food = productList.get(i);
                amount = amount + getTaxValue(food.getAmount(), food.getCgst_tax(), food.getProductQty());
                Log.e("PROD TAX AMOUNT", "" + amount);

                for (int j = 0; j < addOnList.size(); j++) {
                    AddOn addOn = addOnList.get(j);
                    if (food.getProduct_id().equals(addOn.getMain_product_id())) {
                        amount = amount + getTaxValue(addOn.getAmount(), food.getCgst_tax(), addOn.getProduct_qty());
                        Log.e("ADDON TAX AMOUNT", "" + amount);
                    }
                }
            }

            // txtTaxValues.setText("Tax(ST " + myPreferences.getSGST_TaxValue(mActivity) + "% / VAT " + myPreferences.getCGST_TaxValue(mActivity) + "%)");
        }
        return DecimalCheck.decimalFormat(amount);
    }

    private double getTaxValue(String value, String tax, String qty) {
        double val = 0;
        if (tax.length() > 0) {
            if (value.length() > 0) {
                val = Double.parseDouble(value);
                val = val * Double.parseDouble(tax) / 100 * Integer.parseInt(qty);
            }
        } else {
            val = 0;
        }

        return val;
    }

    //private double getTaxValueVAT(String value, String tax) {
      //  double val = 0;
       //         val = Double.parseDouble(value);
       //         val = val * Double.parseDouble(tax) / 100 ;
       // return val;
    //}


    private String getSubTotalAmount(String val) {
        DecimalFormat format = new DecimalFormat("0.00");
        if (val.length() > 0) {
            return format.format(Double.parseDouble(val));

        } else {
            return format.format(0);
        }
    }



    private String getTotalAMount(String SubTotalAmount, String TaxAmount, String PackagingAmount,
                                  String ServiceAmount, double DiscountAmount) {
        double value = 0;
        // Subtotal Amount
        if (SubTotalAmount.length() > 0) {
            value = value + Double.parseDouble(SubTotalAmount);
        }
        if (TaxAmount.length() > 0 && myPreferences.isTaxStatus(mActivity)) {
            value = value + Double.parseDouble(TaxAmount);
        }
        // Packaging Amount
        if (PackagingAmount.length() > 0) {
            value = value + Double.parseDouble(PackagingAmount);
        }
        // Service Charge Amount
        if (ServiceAmount.length() > 0) {
            value = value + Double.parseDouble(ServiceAmount);
        }
        // Flat Discount
        if (DiscountAmount > 0) {
            value = value - DiscountAmount;
        }
        return DecimalCheck.decimalFormat(value);
    }

    private String getPackingCharge(String val) {
        try {
            DecimalFormat format = new DecimalFormat("0.00");
            if (val.length() > 0) {
                return format.format(Double.parseDouble(val));//TODO  java.lang.IllegalArgumentException: Bad class: class java.lang.String
            } else {
                return format.format(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "0.00";
        }
    }

    private String getServiceCharge(String val) {
        try {
            DecimalFormat format = new DecimalFormat("0.00");
            if (val.length() > 0) {
                return format.format(Double.parseDouble(val));
            } else {
                return format.format(0);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return "0.00";
        }
    }

    private String getOrderList() {
        String orderList = "";
        try {
            for (int i = 0; i < productList.size(); i++) {
                Food food = productList.get(i);
                Double amount_tot2 = 0.0;
                amount_tot2 = Double.parseDouble(food.getAmount()) * Integer.parseInt(food.getProductQty());
                if (orderList.length() == 0) {
                    orderList = food.getProduct_id() + "," + String.valueOf(amount_tot2) + "," + food.getProductQty() + ",false," + food.getDiscount() + "," + food.getCategory_id();
                } else {
                    orderList = orderList + "~" + food.getProduct_id() + "," + String.valueOf(amount_tot2) + "," + food.getProductQty() + ",false," + food.getDiscount() + "," + food.getCategory_id();
                }
            }
            for (int i = 0; i < addOnList.size(); i++) {
                AddOn food = addOnList.get(i);
                int amount_tot = 0;
                Log.e("PRODUCT COST", "" + amount_tot);
                amount_tot = Integer.parseInt(food.getAmount()) * Integer.parseInt(food.getProduct_qty());
                if (orderList.length() == 0) {
                    orderList = food.getAddon_id() + "," + String.valueOf(amount_tot) + "," + food.getProduct_qty() + ",true," + food.getMain_product_id() + "," + food.getMain_product_id();
                } else {
                    orderList = orderList + "~" + food.getAddon_id() + "," + String.valueOf(amount_tot) + "," + food.getProduct_qty() + ",true," + food.getMain_product_id() + "," + food.getMain_product_id();
                }
                Log.e("PRODUCT COST", "" + orderList.toString());
            }

        }catch (NumberFormatException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        return orderList;
    }

    private String getOrderType() {
        if (myPreferences.getHomeDelivery(mActivity).equals("1")) {
            return "0";
        } else {
            return "1";
        }
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btnApplyPromo:

                    String coupan = editPromocode.getText().toString().toUpperCase();

                    if (!editPromocode.isEnabled()) {
                        Toast.makeText(mActivity, getResources().getString(R.string.click_to_edit_promo_code), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (coupan.equals("")) {
                        Toast.makeText(mActivity, getResources().getString(R.string.enter_promo_code), Toast.LENGTH_SHORT).show();
                        return;
                    } else if (coupan.equals(couponCode)) {
                        Toast.makeText(mActivity, getResources().getString(R.string.promo_code_already_applied), Toast.LENGTH_SHORT).show();
                        editPromocode.setEnabled(false);
                        imgEditPromoCode.setVisibility(View.VISIBLE);
                        return;
                    }

//                for (CheckoutDeals checkoutDealsCoupan : coupanWithCategoryList) {
//                    if (checkoutDealsCoupan.getCoupanCode().contains(coupan)) {
//                        discAmount = getCoupanDiscount(checkoutDealsCoupan.getDealCategoryId(), coupan);
//                        break;
//                    } else {
//                        imgEditPromoCode.setVisibility(View.GONE);
//                        editPromocode.setVisibility(View.VISIBLE);
//                        Toast.makeText(mActivity, getResources().getString(R.string.invalid_promo_code), Toast.LENGTH_SHORT).show();
//                    }
//                }

                    if (coupanCodeList.contains(coupan)) {
                        if (discAmount > 0) {
                            discAmount = getCoupanDiscount(coupan);
                        } else {
                            discAmount = getCoupanDiscount(coupan);
                        }
                    } else {
                        imgEditPromoCode.setVisibility(View.GONE);
                        editPromocode.setVisibility(View.VISIBLE);
                        Toast.makeText(mActivity, getResources().getString(R.string.invalid_promo_code), Toast.LENGTH_SHORT).show();
                    }
                    break;

                case R.id.btnCancelOrder:
                    Util.getInstance().openFragment(getActivity(), FragmentsName.FRAGMENT_CATEGORIES, FragmentsName.CATEGORIES_ID, null);
                    break;

                case R.id.btnConfirmOrder:

                    if (myPreferences.getHomeDelivery(mActivity).equalsIgnoreCase("1")) {
                        try {
                            double min_order = 30;
                            //double min_order = Double.parseDouble(myPreferences.getMinOrder(mActivity));
                            double tempTotalAmount = Double.valueOf(totalAmount);
                            if (min_order > tempTotalAmount) {
                                Toast.makeText(mActivity, getResources().getString(R.string.min_order_one) + min_order + getResources().getString(R.string.min_order_two), Toast.LENGTH_LONG).show();
                                return;
                            }
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }

                    if (paytmEnabled) {
                        paytmorderid = "";
                        mPaymentWebView.setVisibility(View.VISIBLE);

                        mPaymentWebView.setWebViewClient(
                                new WebViewClient() {
//                                ProgressDialog progressDialog;

                                    @Override
                                    public boolean shouldOverrideUrlLoading(WebView view, String url) {
                                        view.loadUrl(url);
                                        Log.e("Current URL::", view.getUrl());
                                        if (view.getUrl().contains("paytm_url.php")) {
                                            String local_redirectURL = view.getUrl();
                                            String splitArray[] = local_redirectURL.split("res=");
                                            Log.e("Splitted Data : ", splitArray[1]);
                                            // Decode data on other side, by processing encoded data
                                            byte[] valueDecoded = Base64.decode(splitArray[1], 0);
                                            Log.e("Decoded value is ", new String(valueDecoded));
                                            String response = new String(valueDecoded);
                                            try {
                                                Paytm paytmResponse = new Paytm(new JSONObject(response));
                                                Log.e("Paytm Response", paytmResponse.getStatus());
                                                if (paytmResponse.getOrderid().equalsIgnoreCase(paytmorderid) &&
                                                        paytmResponse.getStatus().equalsIgnoreCase("TXN_SUCCESS")) {

                                                    mPaymentWebView.setVisibility(View.GONE);

                                                    hitPlaceOrder(mActivity,
                                                            Const.APP_ID,
                                                            myPreferences.getBranchId(mActivity),
                                                            couponCode,
                                                            myPreferences.getUserMobile(mActivity),
                                                            addressId,
                                                            txtTaxAmount.getText().toString(),
                                                            subTotalAMount,
                                                            String.valueOf(dataHandlerDB.getTotalQty()),
                                                            totalAmounttmp,
                                                            "" + discAmount, // discounts
                                                            instructions, //Instructions
                                                            myPreferences.getUserName(mActivity),
                                                            myPreferences.getAddressEmailid(mActivity), // email is taken from address
                                                            getOrderList(),//OrderList
                                                            myPreferences.getIMEI(mActivity),
                                                            getOrderType(), // Order type
                                                            "PAYTM",
                                                            "",// Paypal response
                                                            packingAmount,
                                                            serviceCHarge,String.valueOf(vatpercentage));
                                                    /*
                                                    hitPlaceOrder(mActivity,
                                                            Const.APP_ID,
                                                            myPreferences.getBranchId(mActivity),
                                                            couponCode,
                                                            myPreferences.getUserMobile(mActivity),
                                                            addressId,
                                                            txtTaxAmount.getText().toString(),
                                                            subTotalAMount,
                                                            String.valueOf(dataHandlerDB.getTotalQty()),
                                                            totalAmount,
                                                            "" + discAmount, // discounts
                                                            instructions, //Instructions
                                                            myPreferences.getUserName(mActivity),
                                                            myPreferences.getAddressEmailid(mActivity), // email is taken from address
                                                            getOrderList(),//OrderList
                                                            myPreferences.getIMEI(mActivity),
                                                            getOrderType(), // Order type
                                                            "PAYTM",
                                                            "",// Paypal response
                                                            packingAmount,
                                                            serviceCHarge,"5");
                                                     */

                                                } else {
                                                    Toast.makeText(mActivity, "Payment failed", Toast.LENGTH_SHORT).show();
                                                    mPaymentWebView.setVisibility(View.GONE);
                                                }
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            } catch (NumberFormatException e) {
                                                e.printStackTrace();
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                        }
                                        return true;
                                    }
                                }
                        );
                        String paymentUrl = "http://aoldev.apponlease.com/api/aolapi/paytm.php";

                        String date = new SimpleDateFormat("yyMMddHHmmss").format(Calendar.getInstance().getTime());
                        paytmorderid = "ORD" + date;

                        String paytmRequest = ServiceURL.encodeBuilder(new String[]{
                                        "ORDER_ID", "TXN_AMOUNT", "app_id", "branch_id"},
                                paytmorderid,
                                totalAmount,
                                Const.APP_ID,
                                myPreferences.getBranchId(mActivity));

                        mPaymentWebView.getSettings().setJavaScriptEnabled(true); // enable javascript

                        // String paymentUrl = "http://aoldev.apponlease.com/api/aolapi/paytm.php?ORDER_ID=" + paytmorderid + "&TXN_AMOUNT=" + totalAmount + "&app_id=" + Const.APP_ID + "&branch_id=16";
                        Log.e("Payment URL", paymentUrl + paytmRequest);
                        mPaymentWebView.loadUrl(paymentUrl + paytmRequest);
                    } else {
                        hitPlaceOrder(mActivity,
                                Const.APP_ID,
                                myPreferences.getBranchId(mActivity),
                                couponCode,
                                myPreferences.getUserMobile(mActivity),
                                addressId,
                                txtTaxAmount.getText().toString(),
                                subTotalAMount,
                                String.valueOf(dataHandlerDB.getTotalQty()),
                                totalAmounttmp,
                                "" + discAmount, // discounts
                                instructions, //Instructions
                                myPreferences.getUserName(mActivity),
                                "", // email is taken from address
                                getOrderList(),//OrderList
                                myPreferences.getIMEI(mActivity),
                                getOrderType(), // Order type
                                "COD",
                                "",// Paypal response
                                packingAmount,
                                serviceCHarge,String.valueOf(vatpercentage));

                        /*
                            hitPlaceOrder(mActivity,
                                Const.APP_ID,
                                myPreferences.getBranchId(mActivity),
                                couponCode,
                                myPreferences.getUserMobile(mActivity),
                                addressId,
                                txtTaxAmount.getText().toString(),
                                subTotalAMount,
                                String.valueOf(dataHandlerDB.getTotalQty()),
                                totalAmount,
                                "" + discAmount, // discounts
                                instructions, //Instructions
                                myPreferences.getUserName(mActivity),
                                "", // email is taken from address
                                getOrderList(),//OrderList
                                myPreferences.getIMEI(mActivity),
                                getOrderType(), // Order type
                                "COD",
                                "",// Paypal response
                                packingAmount,
                                serviceCHarge,"5");
                         */
                    }
                    break;

                case R.id.ll_instructions:
                    showInstructionsDialog();
                    break;

                case R.id.imgEditPromoCode:
                    editPromocode.setEnabled(true);
                    //   txtPromoCode.setVisibility(View.GONE);
                    imgEditPromoCode.setVisibility(View.GONE);
                    break;

            }
        }catch (NumberFormatException e){
                e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private double getCoupanDiscount(String coupan) {

        double discountAmount = 0;
        int pos = coupanCodeList.indexOf(coupan);
        try {
            for (int i = 0; i < productList.size(); i++) {
                Food food = productList.get(i);
                CheckoutDeals checkoutDeals1 = checkoutDealsArrayList.get(pos);

                if ((food.getCategory_id().equals(checkoutDeals1.getDealCategoryId())|| checkoutDeals1.getDealCategoryId().contains(food.getCategory_id())) && checkoutDeals1.getDealType().equals("c")) {
                    double currentProductDiscount = (Double.parseDouble(food.getAmount())
                            * Double.parseDouble(checkoutDeals1.getDealDiscount())) / 100
                            * Double.parseDouble(food.getProductQty());
                    discountAmount = discountAmount + currentProductDiscount;

                    for (int k = 0; k < addOnList.size(); k++) {
                        AddOn addOn = addOnList.get(k);
                        if (food.getProduct_id().equals(addOn.getMain_product_id())) {
                            discountAmount = discountAmount + (Double.parseDouble(addOn.getAmount())
                                    * Double.parseDouble(checkoutDeals1.getDealDiscount())) / 100
                                    * Double.parseDouble(addOn.getProduct_qty());
                        }
                    }
                }
            }
            Log.e("TOTAL COUPAN DISCOUNT", ":::  " + discountAmount);
            if (discountAmount == 0) {
                Toast.makeText(mActivity, getResources().getString(R.string.coupan_not_applicable), Toast.LENGTH_SHORT).show();
            } else if (discAmount <= discountAmount) {
                if (discAmount != 0) {
                    if (discountAmount != discAmount) {
                        showDiscountDialog(coupan, discountAmount);
                    }
                } else {
                    couponCode = coupan;
                    discAmount = discountAmount;
                    txtDiscountAmount.setText(DecimalCheck.decimalFormat(discAmount));
                    totalAmount = getTotalAMount(subTotalAMount, taxAmount, packingAmount, serviceCHarge, discAmount);

                    vattaxAmount=Double.parseDouble(subTotalAMount)*vatpercentage/100;
                    txtsubtotaltax.setText(String.valueOf(vattaxAmount));

                    totalAmounttmp=String.valueOf(Double.parseDouble(totalAmount)+vattaxAmount);
                    txtTotalAMount.setText(totalAmounttmp);

                   // txtTotalAMount.setText(totalAmount);
                    editPromocode.setEnabled(false);
                    imgEditPromoCode.setVisibility(View.VISIBLE);

                    Toast.makeText(mActivity, getResources().getString(R.string.promo_code_applied_successfully), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mActivity, getResources().getString(R.string.higer_discount), Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            discAmount = 0;
        }

        Log.e("TOTAL COUPAN DISCOUNT", ":::111  " + discAmount);
        return discAmount;
    }

    private void showInstructionsDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        final LayoutInflater inflater = mActivity.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_instructions, null);
        builder.setView(dialogView);
        builder.setCancelable(true);

        alert = builder.create();
        final EditText editInstructions = (EditText) dialogView.findViewById(R.id.edit_name);

        if (!txtInstructions.getText().toString().equals("Add Instructions")) {
            editInstructions.setText(instructions);
            if (editInstructions.getText().length() > 0) {
                editInstructions.setSelection(instructions.length());
            }

        }

        Button btnSubmitInstructions = (Button) dialogView.findViewById(R.id.btnSubmitInstruction);
        Button btnCancelInstructions = (Button) dialogView.findViewById(R.id.btnCancelInstruction);

        btnSubmitInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                instructions = editInstructions.getText().toString().trim();
//                if (instructions.isEmpty()) {
//                    Toast.makeText(mActivity, getResources().getString(R.string.provide_instruction), Toast.LENGTH_SHORT).show();
//                    return;
//                }
                if (!(character_Pattern.matcher(instructions).matches())) {
                    Toast.makeText(mActivity, getResources().getString(R.string.please_enter_valid_instructions), Toast.LENGTH_SHORT).show();
                    return;
                }
                txtInstructions.setText(instructions);
                alert.dismiss();
            }
        });

        btnCancelInstructions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });

        if (alert != null) {
            alert.show();
        }

    }

    private void showDiscountDialog(final String coupan, final double discountAmount) {
        try {
            final AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
            builder.setMessage(getResources().getString(R.string.discountRemove))
                    .setCancelable(false)
                    .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                        public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            discAmount = discountAmount;
                            txtDiscountAmount.setText(DecimalCheck.decimalFormat(discAmount));

                            totalAmount = getTotalAMount(subTotalAMount, taxAmount, packingAmount, serviceCHarge, discAmount);

                            vattaxAmount=Double.parseDouble(subTotalAMount)*vatpercentage/100;
                            txtsubtotaltax.setText(String.valueOf(vattaxAmount));

                            totalAmounttmp=String.valueOf(Double.parseDouble(totalAmount)+vattaxAmount);
                            txtTotalAMount.setText(totalAmounttmp);

                            //txtTotalAMount.setText(totalAmount);
                            couponCode = coupan;
                            editPromocode.setEnabled(false);
                            imgEditPromoCode.setVisibility(View.VISIBLE);
                        }
                    })
                    .setNegativeButton(getResources().getString(R.string.no), new DialogInterface.OnClickListener() {
                        public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                            dialog.cancel();
                            editPromocode.setText("");
                            imgEditPromoCode.setVisibility(View.GONE);
                            couponCode = "";

                        }
                    });
            if (alert == null || !alert.isShowing()) {
                alert = builder.create();
                alert.show();
            }
        }catch (NumberFormatException e){
            e.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void hitCheckoutDesls(final Activity mActivity, String branchId) {

        try {

            if (Appstatus.getInstance(mActivity).isOnline()) {
                ProgressDialogUtils.startProgressBar(mActivity, getResources().getString(R.string.pleasewait));

                JSONObject setRequestCustomerAddress = JsonRequestAll.setRequestCheckOutDeals(branchId);
                Log.e(TAG + " REQUEST:::", "" + setRequestCustomerAddress);

                final WebServiceAbstract webServiceAbstract = new WebServiceAbstract() {
                    @Override
                    public void onComplete(JSONObject jsonObject) {
                        try {
                            if (jsonObject != null) {
                                Log.e(TAG, jsonObject.toString());
                                ProgressDialogUtils.stopProgress();
                                afterCheckoutResponse(jsonObject);
                            } else {
                                ProgressDialogUtils.stopProgress();
                                Toast.makeText(mActivity, getResources().getString(R.string.unabletoproceed), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                WebServiceClient.callPOSTService(ServiceURL.CHECKOUT_DEALS,
                        setRequestCustomerAddress, Const.JSON_OBJECT_RESPONSE, myPreferences.getAolToken(mActivity), webServiceAbstract, Request.Method.POST);

            } else {
                Toast.makeText(mActivity, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                ProgressDialogUtils.stopProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void afterCheckoutResponse(JSONObject jsonObject) {

        CheckoutDeals checkoutDeals = new CheckoutDeals(jsonObject);
        if (checkoutDeals.getStatus() == 1) {
            checkoutDealsArrayList = checkoutDeals.getCheckoutDealses();
            coupanCodeList = checkoutDeals.getCoupanCodeList();

            txtDiscountAmount.setText(DecimalCheck.decimalFormat(getFlatDiscount()));
            //getCoupanDiscount(0);

            totalAmount = getTotalAMount(subTotalAMount, taxAmount, packingAmount, serviceCHarge, getFlatDiscount());

            vattaxAmount=Double.parseDouble(subTotalAMount)*vatpercentage/100;
            txtsubtotaltax.setText(String.valueOf(vattaxAmount));

            totalAmounttmp=String.valueOf(Double.parseDouble(totalAmount)+vattaxAmount);
            txtTotalAMount.setText(totalAmounttmp);

           // txtTotalAMount.setText(totalAmount);
        }

    }

    private double getFlatDiscount() {

        double discountAmount = 0;

        try {
            for (int i = 0; i < productList.size(); i++) {
                Food food = productList.get(i);
                for (int j = 0; j < coupanCodeList.size(); j++) {
                    CheckoutDeals checkoutDeals1 = checkoutDealsArrayList.get(j);
                    if ((food.getCategory_id().equals(checkoutDeals1.getDealCategoryId())
                            || checkoutDeals1.getDealCategoryId().contains(food.getCategory_id()))
                            && checkoutDeals1.getDealType().equals("f")) {
                        double currentProductDiscount = (Double.parseDouble(food.getAmount())
                                * Double.parseDouble(checkoutDeals1.getDealDiscount())) / 100
                                * Double.parseDouble(food.getProductQty());
                        discountAmount = discountAmount + currentProductDiscount;

                        appliedDealsMap.put(checkoutDeals1.getDealCategoryId(), currentProductDiscount);

                        for (int k = 0; k < addOnList.size(); k++) {
                            AddOn addOn = addOnList.get(k);
                            if (food.getProduct_id().equals(addOn.getMain_product_id())) {
                                discountAmount = discountAmount + (Double.parseDouble(addOn.getAmount())
                                        * Double.parseDouble(checkoutDeals1.getDealDiscount())) / 100
                                        * Double.parseDouble(addOn.getProduct_qty());
                            }
                        }
                    }
                }
            }
            Log.e("Applied Coupan ", appliedDealsMap.size() + "");
            Log.e("TOTAL FLAT DISCOUNT", ":::  " + discountAmount);
            if (discAmount < discountAmount) {
                discAmount = discountAmount;
            }
        } catch (Exception e) {
            e.printStackTrace();
            discAmount = 0;
        }

        Log.e("TOTAL FLAT DISCOUNT", ":::11  " + discAmount);
        return discAmount;
    }


    private void hitPlaceOrder(final Activity mActivity, String appID, String branchId,
                               String coupon_code, String register_mobile, String address_id,
                               String tax_amount, String actual_amount, String total_item,
                               String total_amount, String discounted_amount, String instruction,
                               String customer_name, String email_id, String orderlist,
                               String imei, String order_Type, String payment_mode, String res,
                               String delivery_charge, String service_charge, String vat) {

        try {

            if (Appstatus.getInstance(mActivity).isOnline()) {
                ProgressDialogUtils.startProgressBar(mActivity, getResources().getString(R.string.pleasewait));

                JSONObject setRequestCustomerAddress = JsonRequestAll.createJsonParamPlaceOrder(appID,
                        branchId, coupon_code, register_mobile, address_id, tax_amount,
                        actual_amount, total_item, total_amount, discounted_amount, instruction,
                        customer_name, email_id, orderlist, imei, order_Type, payment_mode, res,
                        delivery_charge, service_charge,vat);
                Log.e(TAG + " REQUEST:::", "" + setRequestCustomerAddress);
                final WebServiceAbstract webServiceAbstract = new WebServiceAbstract() {
                    @Override
                    public void onComplete(JSONObject jsonObject) {
                        try {
                            if (jsonObject != null) {
                                Log.e(TAG, jsonObject.toString());
                                ProgressDialogUtils.stopProgress();

                                afterResponse(mActivity, jsonObject);

                            } else {
                                ProgressDialogUtils.stopProgress();
                                Toast.makeText(mActivity, getResources().getString(R.string.unabletoproceed), Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
                WebServiceClient.callPOSTService(ServiceURL.PLACE_ORDER,
                        setRequestCustomerAddress, Const.JSON_OBJECT_RESPONSE,
                        myPreferences.getAolToken(mActivity), webServiceAbstract, Request.Method.POST);

            } else {
                Toast.makeText(mActivity, getResources().getString(R.string.nointernet), Toast.LENGTH_SHORT).show();
                ProgressDialogUtils.stopProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void afterResponse(Activity mActivity, JSONObject jsonObject) {

        try {
            Log.d(TAG, "RESPONSE : " + jsonObject);
            if (jsonObject.getInt("status") == 1) {
                Bundle bundle = new Bundle();
                bundle.putString("order_id", jsonObject.getString("order_id"));
                util.openFragment(mActivity, FragmentsName.THANK_YOU, FragmentsName.THANK_YOU_ID, bundle);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private boolean keyboardShown(View rootView) {

        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        return heightDiff > softKeyboardHeight * dm.density;
    }


}
