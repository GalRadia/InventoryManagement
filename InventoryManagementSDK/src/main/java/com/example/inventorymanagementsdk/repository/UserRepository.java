package com.example.inventorymanagementsdk.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inventorymanagementsdk.api.ApiClient;
import com.example.inventorymanagementsdk.api.services.UserService;
import com.example.inventorymanagementsdk.auth.TokenManager;
import com.example.inventorymanagementsdk.models.User;
import com.example.inventorymanagementsdk.responses.DateResponse;
import com.example.inventorymanagementsdk.responses.TokenResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserRepository {
    private UserService userService;
    MutableLiveData<String> expDate = new MutableLiveData<>();
    MutableLiveData<Boolean> timeStamp = new MutableLiveData<>();
    MutableLiveData<List<User>> userMutableLiveData = new MutableLiveData<>();
    MutableLiveData<Boolean> userRegisterMutableLiveData = new MutableLiveData<>();



    public UserRepository() {
        this.userService = ApiClient.getClient().create(UserService.class);
    }

    public LiveData<Boolean> register(User user) {
        userService.register(user).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                userRegisterMutableLiveData.postValue(true);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                userRegisterMutableLiveData.postValue(false);
            }
        });
        return userRegisterMutableLiveData;
    }

    public Call<Void> registerCall(User user) {
        return userService.register(user);
    }

    public LiveData<List<User>> getActiveUsers() {
        userService.getActiveUsers().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                userMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                userMutableLiveData.postValue(null);
            }
        });
        return userMutableLiveData;
    }

    public Call<List<User>> getActiveUsersCall() {
        return userService.getActiveUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        MutableLiveData<List<User>> userMutableLiveData = new MutableLiveData<>();
        userService.getAllUsers().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                userMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                userMutableLiveData.postValue(null);
            }
        });
        return userMutableLiveData;
    }

    public Call<List<User>> getAllUsersCall() {
        return userService.getAllUsers();
    }

    public LiveData<User> getUserById(String id) {
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        userService.getUserById(id).enqueue
                (new Callback<>() {
                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        userMutableLiveData.postValue(response.body());
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        userMutableLiveData.postValue(null);
                    }
                });
        return userMutableLiveData;
    }

    public Call<User> getUserByIdCall(String id) {
        return userService.getUserById(id);
    }

    public LiveData<User> deleteUser(String id) {
        MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
        userService.deleteUser(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                userMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                userMutableLiveData.postValue(null);
            }
        });
        return userMutableLiveData;
    }

    public Call<User> deleteUserCall(String id) {
        return userService.deleteUser(id);
    }

    public LiveData<TokenResponse> login(User user) {
        MutableLiveData<TokenResponse> userMutableLiveData = new MutableLiveData<>();
        userService.login(user).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                if (response.body() == null) {
                    userMutableLiveData.postValue(null);
                    return;
                }
                TokenManager.saveToken(response.body().getToken());
                TokenManager.setManager(response.body().getRole().equals("manager"));
                userMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                userMutableLiveData.postValue(null);
            }
        });
        return userMutableLiveData;
    }


    public LiveData<String> getExpDate() {
        userService.getAuditExp().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<DateResponse> call, Response<DateResponse> response) {
                assert response.body() != null;
                expDate.postValue(response.body().getExp());
            }

            @Override
            public void onFailure(Call<DateResponse> call, Throwable t) {
                expDate.postValue(null);
            }
        });
        return expDate;
    }

    public Call<DateResponse> getExpDateCall() {
        return userService.getAuditExp();
    }

    public LiveData<Boolean> refreshToken() {

        userService.refreshToken().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                TokenManager.saveToken(response.body().getToken());
                timeStamp.postValue(true);
            }

            @Override
            public void onFailure(Call<TokenResponse> call, Throwable t) {
                t.printStackTrace();
                timeStamp.postValue(false);
            }
        });

        return timeStamp;
    }


}
