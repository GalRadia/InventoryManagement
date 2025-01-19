package com.example.inventorymanagementsdk.models;

import com.google.gson.annotations.SerializedName;
import java.sql.Date;

public class Audit {
    @SerializedName("_id")
    private String id;  // Store the ObjectId as String
    private String username;
    private String role;
    private Date timestamp;

    // Constructor
    public Audit(String id, String username, String role, Date timestamp) {
        this.id = id;  // The id corresponds to MongoDB's _id field
        this.username = username;
        this.role = role;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
