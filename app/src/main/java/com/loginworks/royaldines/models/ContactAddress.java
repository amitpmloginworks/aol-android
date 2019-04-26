package com.loginworks.royaldines.models;

import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by lw23 on 24-04-2017.
 */

public class ContactAddress {

    private String id;
    private String address;
    private String location_name;
    private String phone;
    private String email;
    private String webURL;
    private String date;
    private String latitude;
    private String longitude;
    private String img;
    private String url;
    private String path;
    private String status;
    private String message;

    private ArrayList<ContactAddress> contactAddresses;

    public ContactAddress(JSONObject jsonObject) {

        try {
            if (jsonObject.length() > 0) {
                if (jsonObject.has("id")) {
                    setId(jsonObject.getString("id"));
                }

                if (jsonObject.has("address")) {
                    setAddress(jsonObject.getString("address"));
                }

                if (jsonObject.has("location_name")) {
                    setLocation_name(jsonObject.getString("location_name"));
                }

                if (jsonObject.has("phone")) {
                    setPhone(jsonObject.getString("phone"));
                }

                if (jsonObject.has("email")) {
                    setEmail(jsonObject.getString("email"));
                }

                if (jsonObject.has("web_url")) {
                    setWebURL(jsonObject.getString("web_url"));
                }

                if (jsonObject.has("date")) {
                    setDate(jsonObject.getString("date"));
                }

                if (jsonObject.has("latitude")) {
                    setLatitude(jsonObject.getString("latitude"));
                }

                if (jsonObject.has("longitude")) {
                    setLongitude(jsonObject.getString("longitude"));
                }

                if (jsonObject.has("contact_img")) {
                    setImg(jsonObject.getString("contact_img"));
                }

                if (jsonObject.has("url")) {
                    setUrl(jsonObject.getString("url"));
                }

                if (jsonObject.has("path")) {
                    setPath(jsonObject.getString("path"));
                }

                if (jsonObject.has("status")) {
                    setStatus(jsonObject.getString("status"));
                }

                if (jsonObject.has("message")) {
                    setMessage(jsonObject.getString("message"));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public ContactAddress(String id, String address, String location_name, String phone, String email, String webURL, String date, String latitude, String longitude,
                          String img, String url, String path, String status, String message) {
        this.id = id;
        this.address = address;
        this.location_name = location_name;
        this.phone = phone;
        this.email = email;
        this.webURL = webURL;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.img = img;
        this.url = url;
        this.path = path;
        this.status = status;
        this.message = message;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
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

    public String getWebURL() {
        return webURL;
    }

    public void setWebURL(String webURL) {
        this.webURL = webURL;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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

    public ArrayList<ContactAddress> getContactAddresses() {
        return contactAddresses;
    }

    public void setContactAddresses(ArrayList<ContactAddress> contactAddresses) {
        this.contactAddresses = contactAddresses;
    }
}
