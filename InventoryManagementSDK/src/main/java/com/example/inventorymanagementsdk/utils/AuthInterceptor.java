package com.example.inventorymanagementsdk.utils;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;

import androidx.annotation.NonNull;

import com.example.inventorymanagementsdk.auth.TokenManager;

public class AuthInterceptor implements Interceptor {


    public AuthInterceptor() {
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        // Retrieve the token from the TokenManager
        String token = TokenManager.getToken();

        // If the token is not null or empty, add the Authorization header
        if (token != null && !token.isEmpty()) {

            Request newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(newRequest);
        }

        // If there is no token, proceed with the request without the Authorization header
        return chain.proceed(chain.request());
    }
}
