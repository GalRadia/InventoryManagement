package com.example.inventorymanagementsdk.responses;

public class TokenResponse {
    String token;
    String role;

    public TokenResponse(String token) {
        this.token = token;
    }

    public TokenResponse(String token, String role) {
        this.token = token;
        this.role = role;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
