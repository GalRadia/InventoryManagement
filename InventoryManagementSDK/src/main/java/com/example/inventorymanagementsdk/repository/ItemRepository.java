package com.example.inventorymanagementsdk.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.inventorymanagementsdk.api.ApiClient;
import com.example.inventorymanagementsdk.api.services.ItemService;
import com.example.inventorymanagementsdk.models.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItemRepository {
    ItemService itemService;

    public ItemRepository() {
        this.itemService = ApiClient.getClient().create(ItemService.class);
    }
    public LiveData<Item> getItemByIdAsLiveData(String id){
        MutableLiveData<Item> itemMutableLiveData = new MutableLiveData<>();
        itemService.getItemByID(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                itemMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                itemMutableLiveData.postValue(null);
            }
        });
        return itemMutableLiveData;
    }
    public LiveData<List<Item>> getAllItemsAsLiveData(){
        MutableLiveData<List<Item>> itemsMutableLiveData = new MutableLiveData<>();
        itemService.getItems().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                itemsMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                itemsMutableLiveData.postValue(null);
            }
        });
        return itemsMutableLiveData;
    }
    public LiveData<List<Item>> searchItemsAsLiveData(String name){
        MutableLiveData<List<Item>> itemsMutableLiveData = new MutableLiveData<>();
        itemService.searchItems(name).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                itemsMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                itemsMutableLiveData.postValue(null);
            }
        });
        return itemsMutableLiveData;
    }
    public LiveData<Void> insertItem(String name, int quantity, float price, String description){
        MutableLiveData<Void> voidMutableLiveData = new MutableLiveData<>();
        itemService.insertItem(name, quantity, price, description).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                voidMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                voidMutableLiveData.postValue(null);
            }
        });
        return voidMutableLiveData;
    }
    public LiveData<Item> updateItem(String id, Item item){
        MutableLiveData<Item> itemMutableLiveData = new MutableLiveData<>();
        itemService.updateItem(id, item).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                itemMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                itemMutableLiveData.postValue(null);
            }
        });
        return itemMutableLiveData;
    }
    public LiveData<Item> removeItem(String id){
        MutableLiveData<Item> itemMutableLiveData = new MutableLiveData<>();
        itemService.removeItem(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                itemMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                itemMutableLiveData.postValue(null);
            }
        });
        return itemMutableLiveData;
    }

}
