package com.loginworks.royaldines.models;

import org.json.JSONObject;

/**
 * Created by lw23 on 24-04-2017.
 */

public class AboutUs {

    private String category;
    private String description;
    private String message;
    private String status;
    private String errorMessage;

    public AboutUs(JSONObject jsonObject) {

        try {
            if(jsonObject.has("categories")){
                setCategory(jsonObject.getString("categories"));
            }

            if(jsonObject.has("discription")){
                setDescription(jsonObject.getString("discription"));
            }

            if(jsonObject.has("message")){
                setMessage(jsonObject.getString("message"));
            }

            if(jsonObject.has("status")){
                setStatus(jsonObject.getString("status"));
            }

        }catch (Exception ex){
            ex.printStackTrace();
            errorMessage = "There is some problem in our server. Please try after some time.";
        }
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String discription) {
        this.description = discription;
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
