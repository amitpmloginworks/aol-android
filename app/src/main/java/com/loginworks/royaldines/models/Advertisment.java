package com.loginworks.royaldines.models;

import java.util.ArrayList;

/**
 * Created by abc on 04-Apr-17.
 */
public class Advertisment {

    private String image_url;
    private ArrayList<Advertisment> advertismentArrayList;

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public ArrayList<Advertisment> getAdvertismentArrayList() {
        return advertismentArrayList;
    }

    public void setAdvertismentArrayList(ArrayList<Advertisment> advertismentArrayList) {
        this.advertismentArrayList = advertismentArrayList;
    }
}
