package com.example.inventorymanagementsdk.models;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

public class Transaction {
    @SerializedName("_id")

    private String id;
    private String item_id;
    private String item_name;
    private int quantity;
    private float price;
    private String buyer;
    private String timestamp;

    public Transaction( float price, int quantity, String item_id,String item_name) {
        this.item_name = item_name;
        this.price = price;
        this.quantity = quantity;
        this.item_id = item_id;
        this.buyer = buyer;
    }

    public Transaction(String item_id, int quantity,String itemName, float price) {
        this.item_id = item_id;
        this.quantity = quantity;
        this.item_name = itemName;
        this.price = price;
    }

    public String getBuyer() {
        return buyer;
    }

    public void setBuyer(String buyer) {
        this.buyer = buyer;
    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getItem_id() {
        return item_id;
    }

    public void setItem_id(String item_id) {
        this.item_id = item_id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
