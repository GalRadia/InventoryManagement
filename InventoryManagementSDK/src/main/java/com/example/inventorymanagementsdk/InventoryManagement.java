package com.example.inventorymanagementsdk;

import android.content.Context;

public class InventoryManagement {
    private static InventoryManagement instance;

    private static Context context;

    public InventoryManagement(Context context) {
        InventoryManagement.context = context;
    }
    public static synchronized InventoryManagement getInstance(Context context) {
        if (instance == null) {
            instance = new InventoryManagement(context);
        }
        return instance;
    }
    public static Context getContext() {
        return context;
    }
}
