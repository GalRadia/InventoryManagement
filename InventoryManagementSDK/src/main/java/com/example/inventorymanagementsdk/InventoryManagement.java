package com.example.inventorymanagementsdk;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.example.inventorymanagementsdk.auth.TokenManager;
import com.example.inventorymanagementsdk.models.Item;
import com.example.inventorymanagementsdk.models.Transaction;
import com.example.inventorymanagementsdk.repository.ItemRepository;
import com.example.inventorymanagementsdk.repository.TransactionRepository;
import com.example.inventorymanagementsdk.repository.UserRepository;

import java.util.List;

public class InventoryManagement {
    private static InventoryManagement instance;

    private Context context;
    private ItemRepository itemRepository;
    private TransactionRepository transactionRepository;
    private UserRepository userRepository;

    public InventoryManagement() {
    }

    public InventoryManagement(Context context) {
        this.context = context;
        TokenManager.initialize(context);
        itemRepository = new ItemRepository();
        transactionRepository = new TransactionRepository();
        userRepository = new UserRepository();
    }

    public static synchronized InventoryManagement getInstance(Context context) {
        if (instance == null) {
            instance = new InventoryManagement(context);
        }
        return instance;
    }

    public Context getContext() {
        return this.context;
    }

    public LiveData<Item> getItemByIdAsLiveData(String id) {
        return itemRepository.getItemByIdAsLiveData(id);
    }

    public LiveData<List<Item>> getAllItemsAsLiveData() {
        return itemRepository.getAllItemsAsLiveData();
    }

    public LiveData<List<Item>> searchItemsAsLiveData(String name) {
        return itemRepository.searchItemsAsLiveData(name);
    }

    public LiveData<Void> insertItem(String name, int quantity, float price, String description) {
        return itemRepository.insertItem(name, quantity, price, description);
    }

    public LiveData<Item> updateItem(String id, Item item) {
        return itemRepository.updateItem(id, item);
    }

    public LiveData<Item> removeItem(String id) {
        return itemRepository.removeItem(id);
    }

    public LiveData<Void> insertTransaction(String itemId, int quantity) {
        return transactionRepository.addTransaction(itemId, quantity);
    }

    public LiveData<List<Transaction>> getTrending(int limit, int days) {
        return transactionRepository.getTrending(limit, days);
    }

    public LiveData<List<Transaction>> getAllTransactions() {
        return transactionRepository.getTransactions();
    }

    public LiveData<Void> register(String username, String password,String role) {
        return userRepository.register(username, password, role);
    }

    public void login(String username, String password) {
        userRepository.login(username, password);
    }
    public LiveData<List<Transaction>>getTransactionsByUser(String user_name){
        return transactionRepository.getTransactionsByUser(user_name);
    }


}
