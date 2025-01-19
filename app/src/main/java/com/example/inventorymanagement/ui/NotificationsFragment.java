package com.example.inventorymanagement.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.inventorymanagement.adapters.TransactionAdapter;
import com.example.inventorymanagement.databinding.FragmentNotificationsBinding;
import com.example.inventorymanagement.viewModel.InventoryViewModel;
import com.github.mikephil.charting.charts.BarChart;

public class NotificationsFragment extends Fragment {

    private FragmentNotificationsBinding binding;
    private InventoryViewModel inventoryViewModel = null;
    private TransactionAdapter transactionAdapter;
    private RecyclerView recyclerView;
    private BarChart barChart;
    private Button limitDaysButton;
    private SwipeRefreshLayout swipeRefreshLayout;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inventoryViewModel =
                new ViewModelProvider(this).get(InventoryViewModel.class);

        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        initUI();
        setupUI();
        return root;

    }

    private void setupUI() {
        if (inventoryViewModel.isManager()) {
            inventoryViewModel.getAllTransactions().observe(getViewLifecycleOwner(), transactions -> {
                transactionAdapter.setTransactionList(transactions);
                transactionAdapter.notifyDataSetChanged();
            });
        } else {
            inventoryViewModel.getUserTransactions().observe(getViewLifecycleOwner(), transactions -> {
                Log.d("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@", "setupUI: " + transactions);
                transactionAdapter.setTransactionList(transactions);
                transactionAdapter.notifyDataSetChanged();
            });
        }
        limitDaysButton.setOnClickListener(v -> showLimitDaysDialog(getContext()));


    }

    private void initUI() {
        limitDaysButton = binding.chooseDayAndLimitBtn;
        barChart = binding.barChart;
        recyclerView = binding.transactionsRecyclerView;
        swipeRefreshLayout = binding.refresh;
        swipeRefreshLayout.setOnRefreshListener(() -> {
            inventoryViewModel.fetchAllTransactions();
            inventoryViewModel.fetchTransactionByUser();
            swipeRefreshLayout.setRefreshing(false);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        if (inventoryViewModel.isManager())
            transactionAdapter = new TransactionAdapter(getContext(), true);
        else
            transactionAdapter = new TransactionAdapter(getContext(), false);
        recyclerView.setAdapter(transactionAdapter);

    }

    public void showLimitDaysDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Enter Limit and Days");

        // Create a layout
        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(50, 20, 50, 20);

        // Input for Limit
        EditText limitInput = new EditText(context);
        limitInput.setHint("Enter Limit");
        limitInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(limitInput);

        // Input for Days
        EditText daysInput = new EditText(context);
        daysInput.setHint("Enter Days");
        daysInput.setInputType(InputType.TYPE_CLASS_NUMBER);
        layout.addView(daysInput);

        builder.setView(layout);

        // Buttons
        builder.setPositiveButton("OK", (dialog, which) -> {
            String limitText = limitInput.getText().toString().trim();
            String daysText = daysInput.getText().toString().trim();

            // Validate input
            if (!limitText.isEmpty() && !daysText.isEmpty()) {
                int limit = Integer.parseInt(limitText);
                int days = Integer.parseInt(daysText);
                this.inventoryViewModel.getTrending(days, limit).observe(getViewLifecycleOwner(), trends -> {
                    barChart.setData(inventoryViewModel.getBarChartData(trends));
                    barChart.getDescription().setEnabled(false);
                    barChart.getAxisLeft().setDrawGridLines(false);
                    barChart.getAxisRight().setDrawGridLines(false);
                    barChart.getXAxis().setDrawGridLines(false);
                    barChart.getXAxis().setEnabled(false);
                    barChart.invalidate();
                });
            } else {
                Toast.makeText(context, "Please enter both values!", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        builder.show();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}