package com.example.inventorymanagementsdk.api.services;

import com.example.inventorymanagementsdk.models.User;
import com.example.inventorymanagementsdk.responses.DateResponse;
import com.example.inventorymanagementsdk.responses.TokenResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface UserService {
    @POST("auth/login")
    Call<TokenResponse> login(@Body User user);

    @POST("auth/register")
    Call<Void> register(@Body User user);

    @PUT("auth/refresh-token")
    Call<TokenResponse> refreshToken();

    @GET("auth/active")
    Call<List<User>> getActiveUsers();

    @GET("auth/users")
    Call<List<User>> getAllUsers();

    @GET("auth/users/{id}")
    Call<User> getUserById(@Path("id") String id);
    @GET("auth/audit/exp")
    Call<DateResponse> getAuditExp();
    @DELETE("auth/users/{id}")
    Call<User> deleteUser(@Path("id") String id);
}
