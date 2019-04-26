package com.loginworks.royaldines.models;


public class FlatDeals {
    private String name;
    private String discount;
    private String end_date;
    private String description;

    public FlatDeals(String name, String discount, String end_date, String description) {
        this.name = name;
        this.discount = discount;
        this.end_date = end_date;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
