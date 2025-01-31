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
    private final MutableLiveData<List<Item>> itemsMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<Item>>searchItemsMutableLiveData = new MutableLiveData<>();

    MutableLiveData<Item> itemUpdateMutableLiveData = new MutableLiveData<>();

    MutableLiveData<Boolean> booleanMutableLiveData = new MutableLiveData<>();


    public ItemRepository() {
        this.itemService = ApiClient.getClient().create(ItemService.class);
    }

    public LiveData<Item> getItemByIdAsLiveData(String id) {
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

    public Call<Item> getItemByIdCall(String id) {
        return itemService.getItemByID(id);
    }

    public LiveData<List<Item>> getAllItemsAsLiveData() {
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

    public Call<List<Item>> getAllItemsCall() {
        return itemService.getItems();
    }

    public LiveData<List<Item>> searchItemsAsLiveData(String name) {
        itemService.searchItems(name).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Item>> call, Response<List<Item>> response) {
                searchItemsMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<List<Item>> call, Throwable t) {
                searchItemsMutableLiveData.postValue(null);
            }
        });
        return searchItemsMutableLiveData;
    }

    public Call<List<Item>> searchItemsCall(String name) {
        return itemService.searchItems(name);
    }

    public LiveData<Void> insertItem(Item item) {
        MutableLiveData<Void> voidMutableLiveData = new MutableLiveData<>();
        itemService.insertItem(item).enqueue(new Callback<>() {
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

    public Call<Void> insertItemCall(Item item) {
        return itemService.insertItem(item);
    }

    public LiveData<Item> updateItem(String id, Item item) {
        itemService.updateItem(id, item).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Item> call, Response<Item> response) {
                itemUpdateMutableLiveData.postValue(response.body());
            }

            @Override
            public void onFailure(Call<Item> call, Throwable t) {
                itemUpdateMutableLiveData.postValue(null);
            }
        });
        return itemUpdateMutableLiveData;
    }

    public Call<Item> updateItemCall(String id, Item item) {
        return itemService.updateItem(id, item);
    }

    public LiveData<Boolean> removeItem(String id) {
        itemService.removeItem(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                booleanMutableLiveData.postValue(true);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                booleanMutableLiveData.postValue(false);
            }
        });
        return booleanMutableLiveData;
    }

    public Call<Void> removeItemCall(String id) {
        return itemService.removeItem(id);
    }

}
