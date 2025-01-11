package com.example.inventorymanagementsdk.repository;

import static com.example.inventorymanagementsdk.auth.TokenManager.getToken;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inventorymanagementsdk.InventoryManagement;
import com.example.inventorymanagementsdk.api.ApiClient;
import com.example.inventorymanagementsdk.api.services.TransactionService;
import com.example.inventorymanagementsdk.models.Transaction;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionRepository {
    TransactionService transactionService;
    public TransactionRepository() {
        this.transactionService = ApiClient.getClient().create(TransactionService.class);
    }
    public LiveData<Void> addTransaction(String id, int quantity){
     String token = getToken(InventoryManagement.getContext());
        MutableLiveData<Void> transactionMutableLiveData = new MutableLiveData<>();
        transactionService.purchase(token, id, quantity).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                transactionMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                transactionMutableLiveData.postValue(null);
            }
        });
        return transactionMutableLiveData;
    }
    public LiveData<List<Transaction>> getTrending(int limit, int days){
        MutableLiveData<List<Transaction>> transactionsMutableLiveData = new MutableLiveData<>();
        transactionService.getTrending(limit, days).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Transaction>> call, Response<List<Transaction>> response) {
                transactionsMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Transaction>> call, Throwable t) {
                transactionsMutableLiveData.postValue(null);
            }
        });
        return transactionsMutableLiveData;
    }
}
