package com.loginworks.royaldines.models;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ankit Maurya on 4/13/2017.
 */

public class Home implements Serializable {

    private String token;
    private ArrayList<FlatDeals> flatDealsList;
    private ArrayList<HomeBackground> homeBgList;
    private String minOrder;
    private String message;
    private String status;

    private String path;
    private String image_name;

    public Home(JSONObject jsonObject) {
        try {
            flatDealsList = new ArrayList<FlatDeals>();
            homeBgList = new ArrayList<HomeBackground>();
            if (jsonObject.length() > 0) {
                if (jsonObject.has("token")) {
                    token = jsonObject.getString("token");
                }
                if (jsonObject.has("min_order")) {
                    if (jsonObject.getString("min_order").equalsIgnoreCase("null")) {
                        minOrder = "0";
                    } else {
                        minOrder = jsonObject.getString("min_order");
                    }
                }
                if (jsonObject.has("message")) {
                    message = jsonObject.getString("message");
                }
                if (jsonObject.has("status")) {
                    status = jsonObject.getString("status");
                }
                if (jsonObject.has("flat_deals")) {
                    JSONArray flatdealsarray = jsonObject.getJSONArray("flat_deals");
                    if (flatdealsarray.length() > 0) {
                        for (int index = 0; index < flatdealsarray.length(); index++) {
                            JSONObject jsonObjectdeals = flatdealsarray.getJSONObject(index);
                            String name = jsonObjectdeals.getString("name");
                            String discount = jsonObjectdeals.getString("discount");
                            String end_date = jsonObjectdeals.getString("end_date");
                            String description = jsonObjectdeals.getString("description");
                            FlatDeals flatDeals = new FlatDeals(name, discount, end_date, description);
                            flatDealsList.add(flatDeals);
                        }
                    }
                }
                if (jsonObject.has("home_bg")) {
                    JSONArray bgjsonArray = jsonObject.getJSONArray("home_bg");
                    if (bgjsonArray.length() > 0) {
                        for (int index = 0; index < bgjsonArray.length(); index++) {
                            JSONObject bgjsonObject = bgjsonArray.getJSONObject(index);
                            String path = bgjsonObject.getString("path");
                            String imagename = bgjsonObject.getString("imagename");
                            HomeBackground homeBackground = new HomeBackground(path, imagename);
                            homeBgList.add(homeBackground);
                        }
                    }
                }
                Log.e("Flat Deals Size::: ", flatDealsList.size() + "");
                Log.e("Home Background ::: ", homeBgList.size() + "");
            }
        } catch (Exception ex) {

        }
    }


    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getImage_name() {
        return image_name;
    }

    public void setImage_name(String image_name) {
        this.image_name = image_name;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public ArrayList<FlatDeals> getFlatDealsList() {
        return flatDealsList;
    }

    public void setFlatDealsList(ArrayList<FlatDeals> flatDealsList) {
        this.flatDealsList = flatDealsList;
    }

    public ArrayList<HomeBackground> getHomeBgList() {
        return homeBgList;
    }

    public void setHomeBgList(ArrayList<HomeBackground> homeBgList) {
        this.homeBgList = homeBgList;
    }

    public String getMinOrder() {
        return minOrder;
    }

    public void setMinOrder(String minOrder) {
        this.minOrder = minOrder;
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
}
