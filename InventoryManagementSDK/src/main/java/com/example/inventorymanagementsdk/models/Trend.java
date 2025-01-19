package com.example.inventorymanagementsdk.models;

public class Trend {
    private String item_id;
    private String item_name;
    private float total_revenue;
    private int total_sales;

    public Trend(String item_id, String item_name, float total_revenue, int total_sales) {
        this.item_id = item_id;
        this.item_name = item_name;
        this.total_revenue = total_revenue;
        this.total_sales = total_sales;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public float getTotal_revenue() {
        return total_revenue;
    }

    public void setTotal_revenue(float total_revenue) {
        this.total_revenue = total_revenue;
    }

    public int getTotal_sales() {
        return total_sales;
    }

    public void setTotal_sales(int total_sales) {
        this.total_sales = total_sales;
    }
}
