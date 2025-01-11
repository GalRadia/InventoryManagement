package com.example.inventorymanagementsdk.auth;

import android.content.Context;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKeys;

public class TokenManager {

    private static final String PREFS_NAME = "auth_prefs";
    private static final String KEY_TOKEN = "jwt_token";

    public static void saveToken(Context context, String token) {
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

    public static String getToken(Context context) {
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
