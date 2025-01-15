package com.example.inventorymanagementsdk.api.services;

import com.example.inventorymanagementsdk.models.Transaction;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface TransactionService {
    @POST("transaction/purchase")
    Call<Void> purchase( @Body String id, @Body int quantity);

    @GET("transaction/transactions")
    Call<List<Transaction>> getTransactions();
    @GET("transaction/trending")
    Call<List<Transaction>> getTrending(@Body int limit, @Body int days);
    @GET("transaction/transactions/{user_name}")
    Call<List<Transaction>> getTransactionsByUser( @Body String user_name);
}
