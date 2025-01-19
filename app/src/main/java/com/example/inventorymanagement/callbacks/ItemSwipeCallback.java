package com.example.inventorymanagement.callbacks;

import com.example.inventorymanagementsdk.models.Item;

public interface ItemSwipeCallback {
    void onDelete(Item item);
    void onEdit(Item item);
}
