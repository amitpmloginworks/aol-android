package com.loginworks.royaldines.models;

import org.json.JSONObject;

/**
 * Created by lw23 on 20-04-2017.
 */

public class CustomerAddress {

    private int Status;
    private String message;

    public CustomerAddress(JSONObject jsonObject) {

        try {
            setStatus(jsonObject.getInt("status"));
            setMessage(jsonObject.getString("message"));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
