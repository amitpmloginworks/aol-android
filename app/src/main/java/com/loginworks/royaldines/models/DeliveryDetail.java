package com.loginworks.royaldines.models;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by abc on 24-Apr-17.
 */

public class DeliveryDetail {
    private boolean taxStaus;
    private String homeDelivery;
    private String takeAway;
    private String packCharge;
    private String serviceCharge;
    private String otpFailContact;
    private String cgst;
    private String sgst;
    private int status;
    private String message;

    public DeliveryDetail(JSONObject jsonObject) {
        try {
            if (jsonObject.length() > 0) {
                if (jsonObject.getInt("status") == 1) {
                    if (jsonObject.has("tax_status")) {
                        if (jsonObject.getString("tax_status").equalsIgnoreCase("1")) {
                            setTaxStaus(true);
                        } else {
                            setTaxStaus(false);
                        }
                    }
                    if (jsonObject.has("HomeDelivery")) {
                        setHomeDelivery(jsonObject.getString("HomeDelivery"));
                    }
                    if (jsonObject.has("TakeAway")) {
                        setTakeAway(jsonObject.getString("TakeAway"));
                    }
                    if (jsonObject.has("pack_charge")) {
                        setPackCharge(jsonObject.getString("pack_charge"));
                    }
                    if (jsonObject.has("service_charge")) {
                        setServiceCharge(jsonObject.getString("service_charge"));
                    }
                    if (jsonObject.has("otp_fail_contact")) {
                        setOtpFailContact(jsonObject.getString("otp_fail_contact"));
                    }

                    if (jsonObject.has("taxes")) {
                        JSONArray taxArray = jsonObject.getJSONArray("taxes");
                        for (int i = 0; i < taxArray.length(); i++) {
                            JSONObject taxObj = taxArray.getJSONObject(i);
                            if (taxObj.getString("tax_key").equals("SGST")) {
                                setSgst(taxObj.getString("tax_value"));
                            } else if (taxObj.getString("tax_key").equals("CGST")) {
                                setCgst(taxObj.getString("tax_value"));
                            }
                        }
                    }
                }
                if (jsonObject.has("status")) {
                    setStatus(jsonObject.getInt("status"));
                }
                if (jsonObject.has("message")) {
                    setMessage(jsonObject.getString("message"));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public boolean isTaxStaus() {
        return taxStaus;
    }

    public void setTaxStaus(boolean taxStaus) {
        this.taxStaus = taxStaus;
    }

    public String getCgst() {
        return cgst;
    }

    public void setCgst(String cgst) {
        this.cgst = cgst;
    }

    public String getSgst() {
        return sgst;
    }

    public void setSgst(String sgst) {
        this.sgst = sgst;
    }

    public String getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(String serviceCharge) {
        this.serviceCharge = serviceCharge;
    }


    public String getHomeDelivery() {
        return homeDelivery;
    }

    public void setHomeDelivery(String homeDelivery) {
        this.homeDelivery = homeDelivery;
    }

    public String getTakeAway() {
        return takeAway;
    }

    public void setTakeAway(String takeAway) {
        this.takeAway = takeAway;
    }

    public String getPackCharge() {
        return packCharge;
    }

    public void setPackCharge(String packCharge) {
        this.packCharge = packCharge;
    }

    public String getOtpFailContact() {
        return otpFailContact;
    }

    public void setOtpFailContact(String otpFailContact) {
        this.otpFailContact = otpFailContact;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
