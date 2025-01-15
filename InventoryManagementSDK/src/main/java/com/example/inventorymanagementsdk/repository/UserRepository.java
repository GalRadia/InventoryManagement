package com.example.inventorymanagementsdk.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inventorymanagementsdk.InventoryManagement;
import com.example.inventorymanagementsdk.api.ApiClient;
import com.example.inventorymanagementsdk.api.services.UserService;
import com.example.inventorymanagementsdk.auth.TokenManager;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private UserService userService;

    public UserRepository() {
        this.userService = ApiClient.getClient().create(UserService.class);
    }

    public LiveData<Void> register(String username, String password, String role) {
        MutableLiveData<Void> userMutableLiveData = new MutableLiveData<>();
        userService.register(username, password, role).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                userMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                userMutableLiveData.postValue(null);
            }
        });
        return userMutableLiveData;
    }

    public void login(String username, String password) {
        userService.login(username, password).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    String token = response.headers().get("Authorization");
                    TokenManager.saveToken(token);
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void refreshToken() {
        String oldToken = TokenManager.getToken();
        if (oldToken != null) {
            String bearerToken = "Bearer " + oldToken;

            userService.refreshToken(bearerToken).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        String newToken = response.headers().get("Authorization");
                        TokenManager.saveToken(newToken);
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    t.printStackTrace();
                }
            });
        }

    }
}
