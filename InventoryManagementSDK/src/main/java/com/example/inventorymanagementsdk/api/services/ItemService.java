package com.example.inventorymanagementsdk.api.services;

import com.example.inventorymanagementsdk.models.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ItemService {

    @GET("inventory/items")
    Call<List<Item>> getItems();

    @GET("inventory/items/{id}")
    Call<Item> getItemByID(@Path("id") String id);

    @GET("invenroty/search")
    Call<List<Item>> searchItems(@Query("name") String name);

    @POST("inventory/insert_item")
    Call<Void> insertItem(@Body String name, @Body int quantity, @Body float price, @Body String description);

    @PUT("inventory/update_item/{id}")
    Call<Item> updateItem(@Path("id") String id,@Body Item item);
    @DELETE("inventory/remove/{id}")
    Call<Item> removeItem(@Path("id") String id);
}
