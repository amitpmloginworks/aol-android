package com.loginworks.royaldines.models;

import org.json.JSONObject;

/**
 * Created by lw23 on 21-04-2017.
 */

public class OTP {

    private String OTP;
    private String status;
    private String message;

    public OTP(JSONObject jsonObject) {

        try {

            if (jsonObject.getInt("status") == 1) {
                setOTP(jsonObject.getString("otp"));
            }
            setMessage(jsonObject.getString("message"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getOTP() {
        return OTP;
    }

    public void setOTP(String OTP) {
        this.OTP = OTP;
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
}
