package com.loginworks.royaldines.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Ashish Verma on 20-04-2017.
 */

public class Address implements Serializable {

    private String id;
    private String name;
    private String email;
    private String delivery_phone;
    private String house_no;
    private String landmark;
    private String street;
    private String city;
    private String state;
    private String pincode;
    private int status;
    private String message;
    private String errorMessage;
    private ArrayList<Address> addresses = new ArrayList<>();

    public Address(JSONObject jsonObject) {

        try {
            if (jsonObject.has("message")) {
                setMessage(jsonObject.getString("message"));
            }
            if (jsonObject.has("status")) {
                setStatus(jsonObject.getInt("status"));
            }

            if (getStatus() == 1) {

                JSONArray jsonArray = jsonObject.getJSONArray("addresses");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonAddress = jsonArray.getJSONObject(i);

                    Address address = new Address(
                            jsonAddress.getString("id"),
                            jsonAddress.getString("name"),
                            jsonAddress.getString("email"),
                            jsonAddress.getString("delivery_phone"),
                            jsonAddress.getString("house_no"),
                            jsonAddress.getString("landmark"),
                            jsonAddress.getString("street"),
                            jsonAddress.getString("city"),
                            jsonAddress.getString("state"),
                            jsonAddress.getString("pin_code")
                    );
                    addresses.add(address);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            errorMessage = "There is some problem in our server. Please try after some time.";
        }

    }

    public Address(JSONArray jsonArray) {

        try {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonAddress = jsonArray.getJSONObject(i);

                Address address = new Address(
                        jsonAddress.getString("id"),
                        jsonAddress.getString("name"),
                        jsonAddress.getString("email"),
                        jsonAddress.getString("delivery_phone"),
                        jsonAddress.getString("house_no"),
                        jsonAddress.getString("landmark"),
                        jsonAddress.getString("street"),
                        jsonAddress.getString("city"),
                        jsonAddress.getString("state"),
                        jsonAddress.getString("pin_code")
                );
                addresses.add(address);

            }
        } catch (Exception ex) {
            ex.printStackTrace();
            errorMessage = "There is some problem in our server. Please try after some time.";
        }

    }

    public Address(String id, String name, String email, String delivery_phone,
                   String house_no,String landmark, String street, String city, String state, String pincode) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.delivery_phone = delivery_phone;
        this.house_no = house_no;
        this.landmark = landmark;
        this.street = street;
        this.city = city;
        this.state = state;
        this.pincode = pincode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public ArrayList<Address> getAddresses() {
        return addresses;
    }

    public void setAddresses(ArrayList<Address> addresses) {
        this.addresses = addresses;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDelivery_phone() {
        return delivery_phone;
    }

    public void setDelivery_phone(String delivery_phone) {
        this.delivery_phone = delivery_phone;
    }

    public String getHouse_no() {
        return house_no;
    }

    public void setHouse_no(String house_no) {
        this.house_no = house_no;
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

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }
}
