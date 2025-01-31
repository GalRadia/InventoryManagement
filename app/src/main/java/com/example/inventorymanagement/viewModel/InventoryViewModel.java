package com.example.inventorymanagement.viewModel;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.inventorymanagementsdk.InventoryManagement;
import com.example.inventorymanagementsdk.models.Item;
import com.example.inventorymanagementsdk.models.Transaction;
import com.example.inventorymanagementsdk.models.Trend;
import com.example.inventorymanagementsdk.models.User;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;

public class InventoryViewModel extends AndroidViewModel {

    private InventoryManagement inventoryManagement = null;
    private LiveData<String> timestampMutableLiveData;
    private LiveData<List<Transaction>> allTransactionLiveData;
    private LiveData<List<Transaction>> userTransactionsLiveData;
    private LiveData<List<Item>> itemsLiveData;
    private LiveData<List<User>> activeUsersLiveData;
    private LiveData<List<Item>> searchItemsLiveData;
    private MutableLiveData<String> searchQuery = new MutableLiveData<>("");

    private ZonedDateTime targetTime;  // Store target time for countdown

    public InventoryViewModel(@NonNull Application application) {
        super(application);
        // Initialize the SDK instance with the application context
        inventoryManagement = InventoryManagement.getInstance(application);

    }

    public void fetchData() {
        fetchExp();
        fetchItems();
        fetchActiveUsers();
        fetchAllTransactions();
        fetchTransactionByUser();
    }

    public LiveData<Item> updateItem(String id, Item item) {
        return inventoryManagement.updateItem(id, item);
    }

    public LiveData<Boolean> removeItem(String id) {
        return inventoryManagement.removeItem(id);
    }


    public ZonedDateTime getTargetTime() {
        return targetTime;
    }


    public Call<Void> insertItem(String name, int quantity, float price, String description) {
        // Insert an item into the inventory
        return inventoryManagement.insertItemCall(name, quantity, price, description);
    }

    public LiveData<Boolean> refreshToken() {
        return inventoryManagement.refreshToken();
    }

    public void fetchActiveUsers() {
        activeUsersLiveData = inventoryManagement.getActiveUsers();
    }

    public LiveData<List<User>> getActiveUsers() {
        if (activeUsersLiveData == null) {
            fetchActiveUsers();
        }
        return activeUsersLiveData;
    }

    public boolean isManager() {
        return inventoryManagement.isManager();
    }

    public LiveData<List<Item>> getItems() {
        if (itemsLiveData == null) {
            fetchItems();
        }
        return itemsLiveData;
    }

    public void fetchItems() {
        itemsLiveData = inventoryManagement.getAllItemsAsLiveData();
    }

    public void purchaseItem(Item item, int quantity) {
        inventoryManagement.insertTransaction(item.getId(), item.getName(), item.getPrice(), quantity);
    }

    public LiveData<List<Transaction>> getAllTransactions() {
        if (allTransactionLiveData == null) {
            fetchAllTransactions();
        }
        return allTransactionLiveData;
    }

    public LiveData<List<Transaction>> getUserTransactions() {
        if (userTransactionsLiveData == null) {
            fetchTransactionByUser();
        }
        return userTransactionsLiveData;
    }

    public LiveData<String> getExp() {
        if (timestampMutableLiveData == null) {
            fetchExp();
        }
        return timestampMutableLiveData;
    }

    public void fetchAllTransactions() {
        allTransactionLiveData = inventoryManagement.getAllTransactions();

    }

    public void fetchExp() {
        timestampMutableLiveData = inventoryManagement.getAuditExp();
    }

    public void fetchTransactionByUser() {
        userTransactionsLiveData = inventoryManagement.getTransactionsByUser();
    }

    public BarData getBarChartData(List<Trend> trends) {
        return this.inventoryManagement.getBarChartData(trends);
    }

    public LiveData<List<Trend>> getTrending(int limit, int days) {
        return inventoryManagement.getTrending(limit, days);
    }

    public void fetchSearchItems(String name) {
        searchItemsLiveData = inventoryManagement.searchItemsAsLiveData(name);
    }

    public LiveData<List<Item>> getSearchItems(String name) {
        fetchSearchItems(name);

        return searchItemsLiveData;
    }

    public LiveData<List<Item>> getSearchResults() {
        return Transformations.switchMap(
                Transformations.distinctUntilChanged(searchQuery), query -> {
                    if (query == null || query.trim().isEmpty()) {
                        return getItems(); // Show all items when query is empty
                    } else {
                        return getSearchItems(query); // Fetch filtered results
                    }
                }
        );
    }

    public void setSearchQuery(String query) {
        searchQuery.setValue(query);
    }

}
