package com.example.inventorymanagementsdk.auth;

import android.content.Context;
import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

public class TokenManager {

    private static final String PREFS_NAME = "auth_prefs";
    private static final String KEY_TOKEN = "jwt_token";
    private static Context context;
    protected static boolean isManager;

    // Set the application context once
    public static void initialize(Context context) {
        TokenManager.context = context.getApplicationContext();
    }
    public static void setManager(boolean manager) {
        isManager = manager;
    }
    public static boolean isManager() {
        return isManager;
    }

    // Save the token to EncryptedSharedPreferences
    public static void saveToken(String token) {
        if (context == null) {
            throw new IllegalStateException("TokenManager is not initialized with application context.");
        }

        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            EncryptedSharedPreferences sharedPreferences = (EncryptedSharedPreferences) EncryptedSharedPreferences.create(
                    PREFS_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            sharedPreferences.edit().putString(KEY_TOKEN, token).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Get the token from EncryptedSharedPreferences
    public static String getToken() {
        if (context == null) {
            throw new IllegalStateException("TokenManager is not initialized with application context.");
        }

        try {
            String masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC);
            EncryptedSharedPreferences sharedPreferences = (EncryptedSharedPreferences) EncryptedSharedPreferences.create(
                    PREFS_NAME,
                    masterKeyAlias,
                    context,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
            return sharedPreferences.getString(KEY_TOKEN, null);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
