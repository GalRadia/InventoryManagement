package com.example.inventorymanagementsdk.api.services;

import com.example.inventorymanagementsdk.models.Transaction;
import com.example.inventorymanagementsdk.models.Trend;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface TransactionService {
    @POST("transaction/purchase")
    Call<Void> purchase( @Body Transaction transaction);

    @GET("transaction/transactions")
    Call<List<Transaction>> getTransactions();
    @GET("transaction/trending")
    Call<List<Trend>> getTrending(@Query("limit") int limit, @Query("days") int days);
    @GET("transaction/transactions/mine")
    Call<List<Transaction>> getTransactionsByUser();
}
