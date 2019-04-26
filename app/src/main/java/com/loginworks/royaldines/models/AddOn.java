package com.loginworks.royaldines.models;

import com.loginworks.royaldines.databasehandler.DataHandlerDB;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by abc on 11-Apr-17.
 */

public class AddOn implements Serializable {

    private String addon_id;
    private String main_product_id;
    private String category_id;
    private String product_name;
    private String product_img;
    private String discription;
    private String product_type;
    private String amount;
    private String discount;
    private String cgst_tax;
    private String sgst_tax;
    private String swach_bharat_tax;
    private ArrayList<AddOn> addOnArrayList;
    private String product_qty;
    private String message;
    private boolean status;
    private DataHandlerDB dataHandlerDB;

    public AddOn(String addon_id, String main_product_id, String product_qty) {
        this.addon_id = addon_id;
        this.main_product_id = main_product_id;
        this.product_qty = product_qty;
    }

    public AddOn(String addon_id, String category_id, String product_name, String product_img,
                 String discription, String product_type, String amount, String discount, String cgst_tax,
                 String sgst_tax, String swach_bharat_tax, String main_product_id, String product_qty) {
        this.addon_id = addon_id;
        this.category_id = category_id;
        this.product_name = product_name;
        this.product_img = product_img;
        this.discription = discription;
        this.product_type = product_type;
        this.amount = amount;
        this.discount = discount;
        this.cgst_tax = cgst_tax;
        this.sgst_tax = sgst_tax;
        this.swach_bharat_tax = swach_bharat_tax;
        this.main_product_id = main_product_id;
        this.product_qty = product_qty;
    }


    public AddOn(String addon_id, String category_id, String product_name
            , String product_img, String discription, String product_type, String amount,
                 String discount, String cgst_tax, String sgst_tax, String swach_bharat_tax, String main_product_id) {
        this.addon_id = addon_id;
        this.category_id = category_id;
        this.product_name = product_name;
        this.product_img = product_img;
        this.discription = discription;
        this.product_type = product_type;
        this.amount = amount;
        this.discount = discount;
        this.cgst_tax = cgst_tax;
        this.sgst_tax = sgst_tax;
        this.swach_bharat_tax = swach_bharat_tax;
        this.main_product_id = main_product_id;
    }

    public AddOn(String main_product_id, JSONObject jsonObject) {
        try {
            dataHandlerDB = DataHandlerDB.getInstance();
            if (jsonObject.has("message")) {
                setMessage(jsonObject.getString("message"));
            }
            if (jsonObject.has("status")) {
                if (jsonObject.getString("status").equalsIgnoreCase("1")) {
                    setStatus(true);
                } else {
                    setStatus(false);
                }
            }
            if (isStatus()) {
                if (jsonObject.has("add_ons")) {
                    JSONArray addonArray = jsonObject.getJSONArray("add_ons");
                    if (addonArray.length() > 0) {
                        addOnArrayList = new ArrayList<>();
                        for (int addonindex = 0; addonindex < addonArray.length(); addonindex++) {
                            JSONObject addonObject = addonArray.getJSONObject(addonindex);
                            AddOn addOn = new AddOn(
                                    addonObject.getString("addon_id"),
                                    addonObject.getString("category_id"),
                                    addonObject.getString("product_name"),
                                    addonObject.getString("product_img"),
                                    addonObject.getString("discription"),
                                    addonObject.getString("product_type"),
                                    addonObject.getString("amount"),
                                    addonObject.getString("discount"),
                                    addonObject.getString("cgst_tax"),
                                    addonObject.getString("sgst_tax"),
                                    addonObject.getString("swach_bharat_tax"),
                                    main_product_id
                            );
                            addOnArrayList.add(addOn);
                        }
                        dataHandlerDB.insertManageAddOnData(addOnArrayList);
                    }
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getProduct_qty() {
        return product_qty;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }

    public String getAddon_id() {
        return addon_id;
    }

    public void setAddon_id(String addon_id) {
        this.addon_id = addon_id;
    }

    public String getMain_product_id() {
        return main_product_id;
    }

    public void setMain_product_id(String main_product_id) {
        this.main_product_id = main_product_id;
    }

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_img() {
        return product_img;
    }

    public void setProduct_img(String product_img) {
        this.product_img = product_img;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getProduct_type() {
        return product_type;
    }

    public void setProduct_type(String product_type) {
        this.product_type = product_type;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getCgst_tax() {
        return cgst_tax;
    }

    public void setCgst_tax(String cgst_tax) {
        this.cgst_tax = cgst_tax;
    }

    public String getSgst_tax() {
        return sgst_tax;
    }

    public void setSgst_tax(String sgst_tax) {
        this.sgst_tax = sgst_tax;
    }

    public String getSwach_bharat_tax() {
        return swach_bharat_tax;
    }

    public void setSwach_bharat_tax(String swach_bharat_tax) {
        this.swach_bharat_tax = swach_bharat_tax;
    }

    public ArrayList<AddOn> getAddOnArrayList() {
        return addOnArrayList;
    }

    public void setAddOnArrayList(ArrayList<AddOn> addOnArrayList) {
        this.addOnArrayList = addOnArrayList;
    }
}
