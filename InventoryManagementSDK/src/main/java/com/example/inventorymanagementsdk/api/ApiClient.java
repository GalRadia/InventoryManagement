package com.example.inventorymanagementsdk.api;

import com.example.inventorymanagementsdk.utils.AuthInterceptor;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    private static Retrofit retrofit;
    private static final String URL = "https://inventory-management-backend-one-blush.vercel.app/";
    public static Retrofit getClient() {
        if (retrofit == null) {
            // Create an interceptor to log HTTP requests and responses
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);  // Logs the request body

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new AuthInterceptor())
                    .addInterceptor(interceptor)  // Add the interceptor to the client
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(URL)
                    .client(client)  // Set the client
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }



}