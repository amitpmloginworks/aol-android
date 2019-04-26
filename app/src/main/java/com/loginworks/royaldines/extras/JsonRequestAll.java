package com.loginworks.royaldines.extras;

import org.json.JSONObject;

import java.util.HashMap;


public class JsonRequestAll {

    public static JSONObject setRequestMultibranch() {
        JSONObject jobject_multibranch = new JSONObject();
        try {
            jobject_multibranch.put("app_id", Const.APP_ID);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobject_multibranch;
    }

    public static JSONObject createJsonParamsCustomerAddress(String app_id, String register_mobile,
                                                             String username, String email_id,
                                                             String house_no, String landmark,  String street,
                                                             String city, String pincode,
                                                             String delivery_mobile_no) {
        JSONObject jsonObject_customer_address = new JSONObject();
        try {
            jsonObject_customer_address.put("app_id", app_id);
            jsonObject_customer_address.put("mobile", register_mobile);
            jsonObject_customer_address.put("name", username);
            jsonObject_customer_address.put("email", email_id);
            jsonObject_customer_address.put("house_no", house_no);
            jsonObject_customer_address.put("landmark", landmark);
            jsonObject_customer_address.put("street", street);
            jsonObject_customer_address.put("city", city);
            jsonObject_customer_address.put("pin_code", pincode);
            jsonObject_customer_address.put("delivery_phone", delivery_mobile_no);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject_customer_address;
    }

    public static JSONObject setRequestHome(String branch_id) {
        JSONObject jobject_multibranch = new JSONObject();
        try {
            jobject_multibranch.put("app_id", Const.APP_ID);
            jobject_multibranch.put("branch_id", branch_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobject_multibranch;
    }

    public static JSONObject setRequestCategory(String branch_id) {
        JSONObject jobject_multibranch = new JSONObject();
        try {
            jobject_multibranch.put("app_id", Const.APP_ID);
            jobject_multibranch.put("branch_id", branch_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobject_multibranch;
    }

    public static JSONObject setRequestFood(String branch_id, String cat_id) {
        JSONObject jobject_multibranch = new JSONObject();
        try {
            jobject_multibranch.put("app_id", Const.APP_ID);
            jobject_multibranch.put("branch_id", branch_id);
            jobject_multibranch.put("cat_id", cat_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobject_multibranch;
    }

    public static JSONObject setRequestOrderHistory(String mobile) {
        JSONObject jobject_multibranch = new JSONObject();
        try {
            jobject_multibranch.put("app_id", Const.APP_ID);
            jobject_multibranch.put("mobile", mobile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobject_multibranch;
    }

    public static JSONObject setRequestDeliveryDetails(String branch_id) {
        JSONObject jobject_multibranch = new JSONObject();
        try {
            jobject_multibranch.put("app_id", Const.APP_ID);
            jobject_multibranch.put("branch_id", branch_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobject_multibranch;
    }

    public static JSONObject createJsonParamsGetAddress(String appId, String mobile_number) {
        JSONObject jsonObject_getAddress = new JSONObject();
        try {

            jsonObject_getAddress.put("app_id", appId);
            jsonObject_getAddress.put("mobile", mobile_number);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject_getAddress;
    }

    public static JSONObject createJsonParamsGetOTP(String appId, String mobile_number) {
        JSONObject jsonObject_OTP = new JSONObject();
        try {
            jsonObject_OTP.put("app_id", appId);
            jsonObject_OTP.put("mobile_number", mobile_number);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject_OTP;
    }


    public static JSONObject createJsonParamsAboutUs(String app_id, String branch_id) {
        JSONObject jsonObject_about_us = new JSONObject();
        try {
            jsonObject_about_us.put("app_id", app_id);
            jsonObject_about_us.put("branch_id", branch_id);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject_about_us;
    }

    public static JSONObject createJsonParamsContactUs(String branchId) {
        JSONObject jsonObject_about_us = new JSONObject();
        try {
            jsonObject_about_us.put("app_id", Const.APP_ID);
            jsonObject_about_us.put("branch_id", branchId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject_about_us;
    }

    public static JSONObject createJsonParamsVerifyOTP(String appId, String mobile_number, String otp) {
        JSONObject jsonObject_OTP = new JSONObject();
        try {
            jsonObject_OTP.put("app_id", appId);
            jsonObject_OTP.put("mobile", mobile_number);
            jsonObject_OTP.put("otp", otp);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject_OTP;
    }

    public static JSONObject createJsonParamsDeleteAddress(String appId, String mobile_number) {
        JSONObject jsonObject_OTP = new JSONObject();
        try {
            jsonObject_OTP.put("app_id", appId);
            jsonObject_OTP.put("address_id", mobile_number);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject_OTP;
    }

    public static JSONObject createJsonParamPlaceOrder(String... values) {
        JSONObject jsonObject_PlaceOrder = new JSONObject();
        try {
            jsonObject_PlaceOrder.put("app_id", values[0]);
            jsonObject_PlaceOrder.put("branch_id", values[1]);
            jsonObject_PlaceOrder.put("coupon_code", values[2]);
            jsonObject_PlaceOrder.put("phone", values[3]);
            jsonObject_PlaceOrder.put("address_id", values[4]);
            jsonObject_PlaceOrder.put("tax_amount", values[5]);
            jsonObject_PlaceOrder.put("actual_amount", values[6]);
            jsonObject_PlaceOrder.put("total_item", values[7]);
            jsonObject_PlaceOrder.put("total_amount", values[8]);
            jsonObject_PlaceOrder.put("discounted_amount", values[9]);
            jsonObject_PlaceOrder.put("instruction", values[10]);
            jsonObject_PlaceOrder.put("name", values[11]);
            //jsonObject_PlaceOrder.put("email", values[12]);
            jsonObject_PlaceOrder.put("orderlist", values[13]);
            jsonObject_PlaceOrder.put("imei", values[14]);
            jsonObject_PlaceOrder.put("order_Type", values[15]);
            jsonObject_PlaceOrder.put("payment_mode", values[16]);
            jsonObject_PlaceOrder.put("res", values[17]);
            jsonObject_PlaceOrder.put("delivery_charge", values[18]);
            jsonObject_PlaceOrder.put("service_charge", values[19]);
            jsonObject_PlaceOrder.put("vat", values[20]);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject_PlaceOrder;
    }

    public static JSONObject createFCMRegisterRequest(String imei, String regId) {
        JSONObject jobject_multibranch = new JSONObject();
        try {
            jobject_multibranch.put("app_id", Const.APP_ID);
            jobject_multibranch.put("imeiid", imei);
            jobject_multibranch.put("regId", regId);
            jobject_multibranch.put("shareRegId", true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobject_multibranch;
    }

    public static JSONObject setRequestAddOn(String branchId, String categoryId, String productId) {
        JSONObject jsonObject_OTP = new JSONObject();
        try {
            jsonObject_OTP.put("app_id", Const.APP_ID);
            jsonObject_OTP.put("branch_id", branchId);
            jsonObject_OTP.put("cat_id", categoryId);
            jsonObject_OTP.put("proid", productId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return jsonObject_OTP;
    }


    public static JSONObject setRequestCheckOutDeals(String branch_id) {
        JSONObject jobject_multibranch = new JSONObject();
        try {
            jobject_multibranch.put("app_id", Const.APP_ID);
            jobject_multibranch.put("branch_id", branch_id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobject_multibranch;
    }

    public static JSONObject createJsonParamsSupport(String branchId, String st_fname, String st_lname,
                                                     String st_email, String st_mobile, String st_query) {
        JSONObject jobject_support = new JSONObject();
        try {
            jobject_support.put("app_id", Const.APP_ID);
            jobject_support.put("branch_id", branchId);
            jobject_support.put("first_name", st_fname);
            jobject_support.put("last_name", st_lname);
            jobject_support.put("email", st_email);
            jobject_support.put("mobile", st_mobile);
            jobject_support.put("query", st_query);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobject_support;
    }

    public static HashMap<String, String> createJsonParamsPaytm(String branchId, String orderId, String amount) {
        HashMap<String, String> hashmap_paytm = new HashMap<>();
        try {
            hashmap_paytm.put("app_id", Const.APP_ID);
            hashmap_paytm.put("branch_id", branchId);
            hashmap_paytm.put("order_id", orderId);
            hashmap_paytm.put("amount", amount);


        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashmap_paytm;
    }

    public static JSONObject setRequestAddressDetails(String mobile) {
        JSONObject jobject_multibranch = new JSONObject();
        try {
            jobject_multibranch.put("app_id", Const.APP_ID);
            jobject_multibranch.put("mobile", mobile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobject_multibranch;
    }
}

