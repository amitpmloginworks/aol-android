package com.loginworks.royaldines.models;

import android.location.Location;

import com.loginworks.royaldines.databasehandler.DataHandlerDB;
import com.loginworks.royaldines.extras.AppOnLeaseApplication;
import com.loginworks.royaldines.extras.MyPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;


public class BranchParser implements Serializable {

    private int totalBranches;
    private String branch_id;
    private String location_name;
    private String address;
    private String city;
    private String state;
    private String country;
    private String zipcode;
    private String mobile;
    private String phone;
    private String longitude;
    private String latitude;
    private String email;
    private String app_id;
    private String user_id;
    private String status;
    private String message;
    private ArrayList<BranchParser> branchList;
    private String errorMessage;
    private float distance;
    private MyPreferences myPreferences;
    private DataHandlerDB dataHandlerDB;

    public BranchParser(String branch_id, String location_name, String address,
                        String city, String state, String country, String zipcode, String mobile,
                        String phone, String longitude, String latitude, String email,
                        String app_id, String user_id) {

        this.branch_id = branch_id;
        this.location_name = location_name;
        this.address = address;
        this.city = city;
        this.state = state;
        this.country = country;
        this.zipcode = zipcode;
        this.mobile = mobile;
        this.phone = phone;
        this.longitude = longitude;
        this.latitude = latitude;
        this.email = email;
        this.app_id = app_id;
        this.user_id = user_id;
    }

    public BranchParser(JSONObject jsonObject) {
        try {
            myPreferences = MyPreferences.getInstance();
            dataHandlerDB = DataHandlerDB.getInstance();
            branchList = new ArrayList<>();

            if (jsonObject.length() > 0) {
                totalBranches = jsonObject.length();
                if (jsonObject.has("status")) {
                    status = jsonObject.getString("status");
                }
                if (jsonObject.has("message")) {
                    message = jsonObject.getString("message");
                }

                if (jsonObject.has("branches")) {
                    JSONArray jsonArrayBranches = jsonObject.getJSONArray("branches");
                    for (int index = 0; index < jsonArrayBranches.length(); index++) {
                        JSONObject jsonObjectBranches = jsonArrayBranches.getJSONObject(index);
                        branch_id = jsonObjectBranches.getString("branch_id");
                        location_name = jsonObjectBranches.getString("location_name");
                        address = jsonObjectBranches.getString("address");
                        city = jsonObjectBranches.getString("city");
                        state = jsonObjectBranches.getString("state");
                        country = jsonObjectBranches.getString("country");
                        zipcode = jsonObjectBranches.getString("zipcode");
                        mobile = jsonObjectBranches.getString("mobile");
                        phone = jsonObjectBranches.getString("phone");
                        longitude = jsonObjectBranches.getString("longitude");
                        latitude = jsonObjectBranches.getString("latitude");
                        email = jsonObjectBranches.getString("email");
                        app_id = jsonObjectBranches.getString("app_id");
                        user_id = jsonObjectBranches.getString("user_id");

                        BranchParser branchParser = new BranchParser(branch_id, location_name, address,
                                city, state, country, zipcode, mobile, phone, longitude, latitude,
                                email, app_id, user_id);
                        dataHandlerDB.insertBranchData(branchParser);
                        branchList.add(branchParser);

                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            errorMessage = "There is problem in our server. Please try after sone time.";
        }
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<BranchParser> getBranchList() {
        return branchList;
    }

    public void setBranchList(ArrayList<BranchParser> branchList) {
        this.branchList = branchList;
    }

    public int getTotalBranches() {
        return totalBranches;
    }

    public void setTotalBranches(int totalBranches) {
        this.totalBranches = totalBranches;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getApp_id() {
        return app_id;
    }

    public void setApp_id(String app_id) {
        this.app_id = app_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
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

            return Float.parseFloat(decimalFormat.format(currentLoc.distanceTo(branchLoc) / 1000));

        } catch (Exception ex) {
            ex.printStackTrace();
            return 0;
        }


    }

}
