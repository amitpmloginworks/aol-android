package com.loginworks.royaldines.models;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ujjwal on 4/4/2017.
 */

public class CategoriesModel implements Serializable {

    private String catid;
    private String name;
    private String imagename;
    private String imagepath;
    private String description;
    private String message;
    private String status;
    private ArrayList<CategoriesModel> categoriesModelArrayList;

    public CategoriesModel(JSONObject jsonObject) {
        try {
            categoriesModelArrayList = new ArrayList<CategoriesModel>();
            if (jsonObject.length() > 0) {
                if (jsonObject.has("categories")) {
                    JSONArray categoryJsonArray = jsonObject.getJSONArray("categories");
                    if (categoryJsonArray.length() > 0) {
                        for (int index = 0; index < categoryJsonArray.length(); index++) {
                            JSONObject categoryJsonObject = categoryJsonArray.getJSONObject(index);
                            CategoriesModel categoriesModel = new CategoriesModel(
                                    categoryJsonObject.getString("cat_id").toString(),
                                    categoryJsonObject.getString("name").toString(),
                                    categoryJsonObject.getString("img_name").toString(),
                                    categoryJsonObject.getString("img_path").toString(),
                                    categoryJsonObject.getString("description").toString()
                            );
                            categoriesModelArrayList.add(categoriesModel);
                        }
                    }
                }
                if (jsonObject.has("message")) {
                    setMessage(jsonObject.getString("message"));
                }
                if (jsonObject.has("status")) {
                    setStatus(jsonObject.getString("status"));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public CategoriesModel(String catid, String name, String imagename, String imagepath, String description) {
        this.catid = catid;
        this.name = name;
        this.imagename = imagename;
        this.imagepath = imagepath;
        this.description = description;
    }

    public ArrayList<CategoriesModel> getCategoriesModelArrayList() {
        return categoriesModelArrayList;
    }

    public void setCategoriesModelArrayList(ArrayList<CategoriesModel> categoriesModelArrayList) {
        this.categoriesModelArrayList = categoriesModelArrayList;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImagepath() {
        return imagepath;
    }

    public void setImagepath(String imagepath) {
        this.imagepath = imagepath;
    }

    public String getImagename() {
        return imagename;
    }

    public void setImagename(String imagename) {
        this.imagename = imagename;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }
}
