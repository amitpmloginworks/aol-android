package com.loginworks.royaldines.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ujjwal on 4/12/2017.
 */

public class OrderHistoryDetailModel implements Serializable {

    private String priductId;
    private String categoryId;
    private String price;
    private String produstQTY;
    private String addOnParentProdId;
    private String produstName;
    private String productImage;
    private String vat;
    private String swatchBharatTax;
    private String serviceTax;
    private String errorMessage;

    private String currency;
    private String actual_amount;
    private String discounted_amount, tax_amount, total_amount, customername, address, street, city, phone, email, order_instruction, zip, delivery_charge, service_charge, payment_mode;


    private ArrayList<OrderHistoryDetailModel> orderHistoryDetailList;

    public OrderHistoryDetailModel(String priductId, String categoryId, String price,
                                   String produstQTY, String addOnParentProdId, String produstName,
                                   String productImage, String vat, String swatchBharatTax,
                                   String serviceTax) {
        this.priductId = priductId;
        this.categoryId = categoryId;
        this.price = price;
        this.produstQTY = produstQTY;
        this.addOnParentProdId = addOnParentProdId;
        this.produstName = produstName;
        this.productImage = productImage;
        this.vat = vat;
        this.swatchBharatTax = swatchBharatTax;
        this.serviceTax = serviceTax;
    }

    public OrderHistoryDetailModel(JSONObject jsonObject) {
        try {

            setCurrency(jsonObject.getString("currency"));
            setActual_amount(jsonObject.getString("actual_amount"));
            setDiscounted_amount(jsonObject.getString("discounted_amount"));
            setTax_amount(jsonObject.getString("tax_amount"));
            setTotal_amount(jsonObject.getString("total_amount"));
            setCustomername(jsonObject.getString("customername"));
            setAddress(jsonObject.getString("address"));
            setStreet(jsonObject.getString("street"));
            setCity(jsonObject.getString("city"));
            setPhone(jsonObject.getString("phone"));
            setEmail(jsonObject.getString("email"));
            setOrder_instruction(jsonObject.getString("order_instruction"));
            setZip(jsonObject.getString("zip"));
            setDelivery_charge(jsonObject.getString("delivery_charge"));
            setService_charge(jsonObject.getString("service_charge"));
            setPayment_mode(jsonObject.getString("payment_mode"));

            JSONArray jsonArray = jsonObject.getJSONArray("orderdetail");

            orderHistoryDetailList = new ArrayList<>();
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jo = jsonArray.getJSONObject(i);
                    OrderHistoryDetailModel historyDetailModel = new OrderHistoryDetailModel(
                            jo.getString("product_id"),
                            jo.getString("cat_id"),
                            jo.getString("price"),
                            jo.getString("product_qty"),
                            jo.getString("addon_parent_product_id"),
                            jo.getString("product_name"),
                            jo.getString("product_img"),
                            jo.getString("vat"),
                            jo.getString("swach_bharat_tax"),
                            jo.getString("service_tax"));

                    orderHistoryDetailList.add(historyDetailModel);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = "There is some problem in our server. Please try after sone time.";
        }
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public ArrayList<OrderHistoryDetailModel> getOrderHistoryDetailList() {
        return orderHistoryDetailList;
    }

    public void setOrderHistoryDetailList(ArrayList<OrderHistoryDetailModel> orderHistoryDetailList) {
        this.orderHistoryDetailList = orderHistoryDetailList;
    }

    public String getPriductId() {
        return priductId;
    }

    public void setPriductId(String priductId) {
        this.priductId = priductId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProdustQTY() {
        return produstQTY;
    }

    public void setProdustQTY(String produstQTY) {
        this.produstQTY = produstQTY;
    }

    public String getAddOnParentProdId() {
        return addOnParentProdId;
    }

    public void setAddOnParentProdId(String addOnParentProdId) {
        this.addOnParentProdId = addOnParentProdId;
    }

    public String getProdustName() {
        return produstName;
    }

    public void setProdustName(String produstName) {
        this.produstName = produstName;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getVat() {
        return vat;
    }

    public void setVat(String vat) {
        this.vat = vat;
    }

    public String getSwatchBharatTax() {
        return swatchBharatTax;
    }

    public void setSwatchBharatTax(String swatchBharatTax) {
        this.swatchBharatTax = swatchBharatTax;
    }

    public String getServiceTax() {
        return serviceTax;
    }

    public void setServiceTax(String serviceTax) {
        this.serviceTax = serviceTax;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getActual_amount() {
        return actual_amount;
    }

    public void setActual_amount(String actual_amount) {
        this.actual_amount = actual_amount;
    }

    public String getDiscounted_amount() {
        return discounted_amount;
    }

    public void setDiscounted_amount(String discounted_amount) {
        this.discounted_amount = discounted_amount;
    }

    public String getTax_amount() {
        return tax_amount;
    }

    public void setTax_amount(String tax_amount) {
        this.tax_amount = tax_amount;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getCustomername() {
        return customername;
    }

    public void setCustomername(String customername) {
        this.customername = customername;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOrder_instruction() {
        return order_instruction;
    }

    public void setOrder_instruction(String order_instruction) {
        this.order_instruction = order_instruction;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public String getService_charge() {
        return service_charge;
    }

    public void setService_charge(String service_charge) {
        this.service_charge = service_charge;
    }

    public String getPayment_mode() {
        return payment_mode;
    }

    public void setPayment_mode(String payment_mode) {
        this.payment_mode = payment_mode;
    }

}
