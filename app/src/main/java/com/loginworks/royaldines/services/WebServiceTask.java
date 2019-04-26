package com.loginworks.royaldines.services;


import org.json.JSONArray;
import org.json.JSONObject;

public interface WebServiceTask {
    void onComplete(JSONArray jsonArray);

    void onComplete(JSONObject jsonObject);

    void onComplete(String string);
}

