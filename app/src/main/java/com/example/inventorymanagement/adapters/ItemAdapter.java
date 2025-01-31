package com.example.inventorymanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventorymanagement.R;
import com.example.inventorymanagement.callbacks.ItemCallback;
import com.example.inventorymanagement.callbacks.ItemSwipeCallback;
import com.example.inventorymanagement.databinding.ItemLayoutBinding;
import com.example.inventorymanagementsdk.models.Item;

import java.util.ArrayList;
import java.util.List;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ItemViewHolder> {
    private Context context;
    private List<Item> itemList = new ArrayList<>();
    private ItemLayoutBinding binding;
    private ItemCallback itemCallback;
    private ItemSwipeCallback itemSwipeCallback;

    // Constructor
    public ItemAdapter(Context context) {
        this.context = context;
    }

    // Set the callback for item clicks
    public void setItemCallback(ItemCallback itemCallback) {
        this.itemCallback = itemCallback;
    }
    public void setItemSwipeCallback(ItemSwipeCallback itemSwipeCallback) {
        this.itemSwipeCallback = itemSwipeCallback;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = ItemLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ItemViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        Item item = itemList.get(position);
        // Bind data to UI
        holder.itemName.setText(item.getName());
        holder.itemQuantity.setText(String.valueOf(item.getQuantity()));
        holder.itemPrice.setText(String.valueOf(item.getPrice()));
        holder.itemDescription.setText(item.getDescription());

        // Fade animation on item click
        //setFadeAnimation(holder.itemView);

        // Handle item selection
        holder.selectItem.setOnClickListener(v -> {
            if (itemCallback != null) {
                itemCallback.onItemClicked(item);
            }
        });
        holder.itemView.setOnLongClickListener(v -> {
            if (itemSwipeCallback != null) {
                itemSwipeCallback.onEdit(item);
            }
            return true;
        });

    }

    private void setFadeAnimation(View view) {
        Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_scale_animation);
        view.startAnimation(animation);
    }

    @Override
    public int getItemCount() {
        return itemList != null ? itemList.size() : 0;
    }

    // Method to update the data list and notify the adapter
    public void setItems(List<Item> items) {
        this.itemList = items;
        notifyDataSetChanged(); // Notify adapter when data is updated
    }

    // Method to get the current item list (optional)
    public List<Item> getItems() {
        return itemList;
    }

    public void deleteItem(int position) {
        Item item = itemList.get(position);
        itemSwipeCallback.onDelete(item);
        notifyDataSetChanged();

    }

    public Item getItem(int position) {
        return itemList.get(position);
    }

    public void editItem(int position, Item item) {
        itemSwipeCallback.onEdit(item);
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        private TextView itemName;
        private TextView itemQuantity;
        private TextView itemPrice;
        private TextView itemDescription;
        private Button selectItem;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // Bind the UI components
            itemName = binding.itemName;
            itemQuantity = binding.itemQuantity;
            itemPrice = binding.itemPrice;
            itemDescription = binding.itemDescription;
            selectItem = binding.purchaseButton;
        }
    }
}
