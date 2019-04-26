package com.loginworks.royaldines.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Food implements Serializable {


    private String product_id;
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
    private ArrayList<Food> foodArrayList;
    // private ArrayList<AddOn> addOnArrayList;
    private String message;
    private String status;
    private String productqty;
    private HashMap<String, ArrayList<AddOn>> addonHashMap;
    private boolean isAddOn;

    public Food(String product_id, String category_id, String product_name, String product_img,
                String discription, String product_type, String amount, String discount, String cgst_tax,
                String sgst_tax, String swach_bharat_tax, String productqty) {
        this.product_id = product_id;
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
        this.productqty = productqty;
    }

    public Food(String product_id, String category_id, String product_name, String product_img,
                String discription, String product_type, String amount, String discount,
                String cgst_tax, String sgst_tax, String swach_bharat_tax) {
        this.product_id = product_id;
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
    }

    public Food(JSONObject jsonObject) {
        try {
            foodArrayList = new ArrayList<Food>();
            //addOnArrayList = new ArrayList<AddOn>();
            addonHashMap = new HashMap<>();
            if (jsonObject.length() > 0) {
                if (jsonObject.has("products")) {
                    JSONArray productJsonArray = jsonObject.getJSONArray("products");
                    if (productJsonArray.length() > 0) {
                        for (int index = 0; index < productJsonArray.length(); index++) {
                            JSONObject productJsonObject = productJsonArray.getJSONObject(index);
                            Food food = new Food(
                                    productJsonObject.getString("product_id"),
                                    productJsonObject.getString("category_id"),
                                    productJsonObject.getString("product_name"),
                                    productJsonObject.getString("product_img"),
                                    productJsonObject.getString("discription"),
                                    productJsonObject.getString("product_type"),
                                    productJsonObject.getString("amount"),
                                    productJsonObject.getString("discount"),
                                    productJsonObject.getString("cgst_tax"),
                                    productJsonObject.getString("sgst_tax"),
                                    productJsonObject.getString("swach_bharat_tax")
                            );
                            foodArrayList.add(food);
                            if (productJsonObject.has("add_on")) {
                                JSONArray addonArray = productJsonObject.getJSONArray("add_on");
                                if (addonArray.length() > 0) {
                                    ArrayList<AddOn> addOnArrayList = new ArrayList<>();
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
                                                productJsonObject.getString("product_id")
                                        );
                                        addOnArrayList.add(addOn);
                                    }
                                    addonHashMap.put(productJsonObject.getString("product_id"), addOnArrayList);
                                }

                            }
                        }
                    }
                }
                if (jsonObject.has("message")) {
                    setMessage(jsonObject.getString("message"));
                }
                if (jsonObject.has("status")) {
                    setStatus(jsonObject.getString("status"));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isAddOn() {
        return isAddOn;
    }

    public void setAddOn(boolean addOn) {
        isAddOn = addOn;
    }

    public HashMap<String, ArrayList<AddOn>> getAddonHashMap() {
        return addonHashMap;
    }

    public void setAddonHashMap(HashMap<String, ArrayList<AddOn>> addonHashMap) {
        this.addonHashMap = addonHashMap;
    }

    public String getProductqty() {
        return productqty;
    }

    public void setProductqty(String productqty) {
        this.productqty = productqty;
    }

    public String getProductQty() {
        return productqty;
    }

    public void setProductQty(String productqty) {
        this.productqty = productqty;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
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

    public ArrayList<Food> getFoodArrayList() {
        return foodArrayList;
    }

    public void setFoodArrayList(ArrayList<Food> foodArrayList) {
        this.foodArrayList = foodArrayList;
    }


}
