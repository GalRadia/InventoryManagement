package com.example.inventorymanagement.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.inventorymanagement.databinding.TransactionLayoutBinding;
import com.example.inventorymanagementsdk.models.Transaction;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {
    private Context context;
    private List<Transaction> transactionList = new ArrayList<>();
    private TransactionLayoutBinding binding;
    private boolean isManager;

    public TransactionAdapter(Context context,boolean isManager) {
        this.context = context;
        this.isManager = isManager;

    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        binding = TransactionLayoutBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new TransactionViewHolder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        Transaction transaction = transactionList.get(position);
        holder.itemNameTextView.setText(transaction.getItem_name());
        holder.quantityTextView.setText(String.valueOf(transaction.getQuantity()));
        holder.priceTextView.setText(String.valueOf(transaction.getPrice()*transaction.getQuantity()));
        if (isManager) {
            holder.buyerTextView.setVisibility(View.VISIBLE);
        }
        holder.buyerTextView.setText(transaction.getBuyer());
        holder.timestampTextView.setText(transaction.getTimestamp().toString());

    }

    @Override
    public int getItemCount() {
        return transactionList != null ? transactionList.size() : 0;
    }
    public void setTransactionList(List<Transaction> transactionList) {
        this.transactionList = transactionList;
        notifyDataSetChanged();
    }
    public List<Transaction> getTransactionList() {
        return transactionList;
    }


    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        private TextView itemNameTextView;
        private TextView quantityTextView;
        private TextView priceTextView;
        private TextView timestampTextView;
        private TextView buyerTextView;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            itemNameTextView = binding.itemNameTextView;
            quantityTextView = binding.quantityTextView;
            priceTextView = binding.priceTextView;
            timestampTextView = binding.timestampTextView;
            buyerTextView = binding.buyerTextView;
        }

    }

}
