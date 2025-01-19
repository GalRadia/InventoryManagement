package com.example.inventorymanagementsdk.repository;

import static com.example.inventorymanagementsdk.auth.TokenManager.getToken;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inventorymanagementsdk.api.ApiClient;
import com.example.inventorymanagementsdk.api.services.TransactionService;
import com.example.inventorymanagementsdk.models.Transaction;
import com.example.inventorymanagementsdk.models.Trend;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionRepository {
    TransactionService transactionService;
    UserRepository userRepository;
    MutableLiveData<List<Transaction>> transactionsMutableLiveData = new MutableLiveData<>();
    MutableLiveData<List<Trend>> trendsMutableLiveData = new MutableLiveData<>();

    public TransactionRepository() {
        this.transactionService = ApiClient.getClient().create(TransactionService.class);
        userRepository = new UserRepository();
    }

    public LiveData<Void> addTransaction(Transaction transaction) {
        MutableLiveData<Void> transactionMutableLiveData = new MutableLiveData<>();
        transactionService.purchase(transaction).enqueue(new Callback<>() {
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

    public Call<Void> addTransactionCall(Transaction transaction) {
        return transactionService.purchase(transaction);
    }

    public LiveData<List<Trend>> getTrending(int limit, int days) {
        transactionService.getTrending(limit, days).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Trend>> call, Response<List<Trend>> response) {
                Log.w("tag", "onResponse:" + response.body());
                trendsMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Trend>> call, Throwable t) {
                Log.w("tag", "onFailure: " + t.getMessage());
                trendsMutableLiveData.postValue(new ArrayList<>());
            }
        });
        return trendsMutableLiveData;
    }
    public Call<List<Trend>> getTrendingCall(int limit, int days){
        return transactionService.getTrending(limit, days);
    }

    public LiveData<List<Transaction>> getTransactions() {
        transactionService.getTransactions().enqueue(new Callback<>() {
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
    public Call<List<Transaction>> getTransactionsCall(){
        return transactionService.getTransactions();
    }

    public LiveData<List<Transaction>> getTransactionsByUser() {

        transactionService.getTransactionsByUser().enqueue(new Callback<>() {
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
    public Call<List<Transaction>> getTransactionsByUserCall(){
        return transactionService.getTransactionsByUser();
    }

}
