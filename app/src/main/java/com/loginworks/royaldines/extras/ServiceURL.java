package com.loginworks.royaldines.extras;


import android.net.Uri;

public class ServiceURL {

    public static final String NIP = "check/nip";

    //public static final String BASE_URL = "http://food.apponlease.com";  //Production Server URL
    //public static final String BASE_URL = "http://sandbox.apponlease.com";  //sandbox Server URL
    //public static final String MAIN_URL = "http://aoldev.apponlease.com";  //Development Server URL

    //Sandbox Urls
    public static final String BASE_URL = "http://food.apponlease.com/api/aolapi";  //Sandbox Testing Server URL
    public static final String BASE_IMAGE_PATH = "http://food.apponlease.com/";

    
    public static final String PRODUCT_IMAGE_PATH = "gallery/product/";

    public static final String BRANCH = "/getMultiBranch.php";
    public static final String HOME = "/getHomeData.php";
    public static final String CATEGORIES = "/getCategories.php";
    public static final String FOOD = "/getProducts.php";
    public static final String ORDER_HISTORY = "/getOrderHistory.php";
    public static final String GET_ADDRESSES = "/getAddress.php";
    public static final String CUSTOMER_ADDRESS = "/addAddress.php";
    public static final String GET_OTP = "/loginUser.php";
    public static final String VERIFY_OTP = "/verifyOTP.php";
    public static final String DELIVERY_DETAILS = "/getDeliveryDetails.php";
    public static final String PLACE_ORDER = "/placeOrder.php";
    public static final String CHECKOUT_DEALS = "/getCheckoutDeals.php";
    public static final String ABOUT_US = "/aboutUs.php";
    public static final String CONTACT_US = "/contactUs.php";
    public static final String ADDONS = "/getAddOns.php";
    public static final String FCM_REGISTER = "/notification/gcm.php";
    public static final String DELETE_ADDRESS = "/deleteAddress.php";
    public static final String SUPPORT = "/support.php";
    public static final String PAYTM = "/paytm.php";
    public static final String ADDRESS = "/getadd_api.php";


    public static String encodeBuilder(String[] params, String... values) {
        try {
            String url = params[0] + "=" + Uri.encode(values[0].trim(), "utf-8");

            int length = params.length;
            for (int temp = 1; temp < length; temp++) {
                if (values[temp] != null)
                    values[temp] = values[temp].trim();
                url = url + "&" + params[temp] + "=" + Uri.encode(values[temp], "utf-8");
            }
            return "?" + url;
        } catch (Exception ex) {
            ex.printStackTrace();
            return ex.getMessage();
        }
    }
}

