package com.example.inventorymanagementsdk.utils;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import androidx.annotation.NonNull;

import com.example.inventorymanagementsdk.auth.TokenManager;

public class AuthInterceptor implements Interceptor {

    // List of exempt routes
    private static final List<String> EXEMPT_ROUTES = Arrays.asList(
            "/auth/login",
            "/auth/register",
            "/",
            "/favicon.ico",
            "/static/",
            "/templates/",
            "/index.html",
            "/favicon.svg",
            "/favicon.png",
            "site.webmanifest",
            "/site.webmanifest",
            "/favicon-96x96.png",
            "/favicon-32x32.png"
    );

    public AuthInterceptor() {
    }

    @NonNull
    @Override
    public Response intercept(@NonNull Interceptor.Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String urlPath = originalRequest.url().encodedPath(); // Get the encoded path of the URL

        // Check if the URL matches any of the exempt routes
        if (isExemptRoute(urlPath)) {
            // Proceed without adding the Authorization header
            return chain.proceed(originalRequest);
        }

        // Retrieve the token from the TokenManager
        String token = TokenManager.getToken();

        // If the token is not null or empty, add the Authorization header
        if (token != null && !token.isEmpty()) {
            Request newRequest = originalRequest.newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build();
            return chain.proceed(newRequest);
        }

        // If there is no token, proceed with the request without the Authorization header
        return chain.proceed(originalRequest);
    }

    // Helper method to check if a URL path is exempt
    private boolean isExemptRoute(String urlPath) {
        for (String exemptRoute : EXEMPT_ROUTES) {
            if (urlPath.equals(exemptRoute) || (exemptRoute.endsWith("/*") && urlPath.startsWith(exemptRoute.replace("/*", "")))) {
                return true;
            }
        }
        return false;
    }
}
