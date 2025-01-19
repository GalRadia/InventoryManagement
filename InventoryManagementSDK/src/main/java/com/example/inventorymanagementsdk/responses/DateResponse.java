package com.example.inventorymanagementsdk.responses;

public class DateResponse {
    private String exp;

    public DateResponse(String date) {
        this.exp = date;
    }

    public String getExp() {
        return exp;
    }

    public void setExp(String exp) {
        this.exp = exp;
    }
}
