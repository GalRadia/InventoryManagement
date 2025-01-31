package com.example.inventorymanagementsdk;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.example.inventorymanagementsdk.auth.TokenManager;
import com.example.inventorymanagementsdk.models.Item;
import com.example.inventorymanagementsdk.models.Transaction;
import com.example.inventorymanagementsdk.models.Trend;
import com.example.inventorymanagementsdk.models.User;
import com.example.inventorymanagementsdk.repository.ItemRepository;
import com.example.inventorymanagementsdk.repository.TransactionRepository;
import com.example.inventorymanagementsdk.repository.UserRepository;
import com.example.inventorymanagementsdk.responses.DateResponse;
import com.example.inventorymanagementsdk.responses.TokenResponse;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;
import java.util.concurrent.atomic.AtomicInteger;

import retrofit2.Call;

public class InventoryManagement {
    private static InventoryManagement instance;

    private final Context context;
    private final ItemRepository itemRepository;
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

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

    public Call<Item> getItemByIdCall(String id) {
        return itemRepository.getItemByIdCall(id);
    }

    public LiveData<List<Item>> getAllItemsAsLiveData() {
        return itemRepository.getAllItemsAsLiveData();
    }

    public Call<List<Item>> getAllItemsCall() {
        return itemRepository.getAllItemsCall();
    }

    public LiveData<List<Item>> searchItemsAsLiveData(String name) {
        return itemRepository.searchItemsAsLiveData(name);
    }

    public Call<List<Item>> searchItemsCall(String name) {
        return itemRepository.searchItemsCall(name);
    }

    public LiveData<Void> insertItem(String name, int quantity, float price, String description) {
        Item item = new Item(name, quantity, price, description);
        return itemRepository.insertItem(item);
    }

    public Call<Void> insertItemCall(String name, int quantity, float price, String description) {
        Item item = new Item(name, quantity, price, description);
        return itemRepository.insertItemCall(item);
    }


    public LiveData<Item> updateItem(String id, Item item) {
        return itemRepository.updateItem(id, item);
    }

    public Call<Item> updateItemCall(String id, Item item) {
        return itemRepository.updateItemCall(id, item);
    }

    public LiveData<Boolean> removeItem(String id) {
        return itemRepository.removeItem(id);
    }

    public Call<Void> removeItemCall(String id) {
        return itemRepository.removeItemCall(id);
    }

    public LiveData<Void> insertTransaction(String itemId,String itemName,float itemPrice, int quantity) {
        Transaction transaction = new Transaction(itemId, quantity, itemName, itemPrice);
    return transactionRepository.addTransaction(transaction);
    }

    public Call<Void> insertTransactionCall(String itemId, int quantity, float price, String item_name,String buyer) {
        Transaction transaction = new Transaction(price, quantity, itemId, item_name);
        return transactionRepository.addTransactionCall(transaction);
    }

    public LiveData<List<Trend>> getTrending(int limit, int days) {
        return transactionRepository.getTrending(limit, days);
    }

    public Call<List<Trend>> getTrendingCall(int limit, int days) {
        return transactionRepository.getTrendingCall(limit, days);
    }

    public LiveData<List<Transaction>> getAllTransactions() {
        return transactionRepository.getTransactions();
    }

    public Call<List<Transaction>> getAllTransactionsCall() {
        return transactionRepository.getTransactionsCall();
    }

    public LiveData<Boolean> register(String username, String password, String role) {
        User user = new User(username, password, role);
        return userRepository.register(user);
    }

    public Call<Void> registerCall(String username, String password, String role) {
        User user = new User(username, password, role);
        return userRepository.registerCall(user);
    }

    public LiveData<TokenResponse> login(String username, String password) {
        User user = new User(username, password, "");
        return userRepository.login(user);
    }


    public LiveData<List<Transaction>> getTransactionsByUser() {
        return transactionRepository.getTransactionsByUser();
    }

    public Call<List<Transaction>> getTransactionsByUserCall() {
        return transactionRepository.getTransactionsByUserCall();
    }

    public LiveData<String> getAuditExp() {
        return userRepository.getExpDate();
    }
    public LiveData<List<User>> getActiveUsers() {
        return userRepository.getActiveUsers();
    }

    public Call<DateResponse> getAuditExpCall() {
        return userRepository.getExpDateCall();
    }

    public LiveData<Boolean> refreshToken() {
       return userRepository.refreshToken();
    }

    public boolean isManager() {
        return TokenManager.isManager();
    }

    public BarData getBarChartData(List<Trend> trends) {
        BarData barData = new BarData();
        Stack<Integer> colors = getColors();
        Random random = new Random();


        trends.forEach(trend -> {
            int color;
            if (colors.isEmpty()) {
                color = Color.rgb(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            } else {
                color = colors.pop();
            }

            BarDataSet dataSet = new BarDataSet(new ArrayList<BarEntry>() {{
                add(new BarEntry(trends.indexOf(trend), trend.getTotal_revenue()));
            }}, trend.getItem_name());

            dataSet.setColor(color);  // Pass int instead of AtomicInteger
            barData.addDataSet(dataSet);
        });

        return barData;
    }

    private Stack<Integer> getColors() {
        Stack<Integer> colors = new Stack<>();
        for (int color : ColorTemplate.MATERIAL_COLORS) colors.add(color);
        for (int color : ColorTemplate.VORDIPLOM_COLORS) colors.add(color);
        for (int color : ColorTemplate.JOYFUL_COLORS) colors.add(color);
        for (int color : ColorTemplate.COLORFUL_COLORS) colors.add(color);
        for (int color : ColorTemplate.LIBERTY_COLORS) colors.add(color);
        for (int color : ColorTemplate.PASTEL_COLORS) colors.add(color);

        return colors;
    }

}
