package com.example.inventorymanagementsdk.api.services;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface UserService {
    @POST("auth/login")
    Call<Void> login(@Body String username, @Body String password);

    @POST("auth/register")
    Call<Void> register(@Body String username, @Body String password, @Body String role);

    @POST("auth/refresh_token")
    Call<Void> refreshToken(@Body String token);
}
