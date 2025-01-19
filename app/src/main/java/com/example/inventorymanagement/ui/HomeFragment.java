package com.example.inventorymanagement.ui;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.inventorymanagement.databinding.FragmentHomeBinding;
import com.example.inventorymanagement.viewModel.InventoryViewModel;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private TextView textTransactions;
    private TextView textItemsCount;
    private TextView textTokenCountdown;
    private TextView textActiveUsers;
    private Button buttonRefreshToken;
    private InventoryViewModel inventoryViewModel;
    boolean isManager = false;
    private CountDownTimer countDownTimer;



    private Handler countdownHandler = new Handler(Looper.getMainLooper());
    private Runnable countdownRunnable;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        inventoryViewModel =
                new ViewModelProvider(this).get(InventoryViewModel.class);
        inventoryViewModel.fetchData();
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        isManager = inventoryViewModel.isManager();
        initUI();
        setUpUI();
        return root;
    }

    public void initUI() {
        textTransactions = binding.textTransactions;
        textItemsCount = binding.textItemsCount;
        textTokenCountdown = binding.textTokenCountdown;
        textActiveUsers = binding.textActiveUsers;
        buttonRefreshToken = binding.buttonRefreshToken;
    }

    public void setUpUI() {
        if (isManager) {
            textActiveUsers.setVisibility(View.VISIBLE);
            inventoryViewModel.getActiveUsers().observe(getViewLifecycleOwner(), users -> {
                if (users != null) {
                    textActiveUsers.setText("Active users: " + users.size());
                } else {
                    textActiveUsers.setText("Active users: 0");
                }
            });
            inventoryViewModel.getAllTransactions().observe(getViewLifecycleOwner(), transactions -> {
                if (transactions != null) {
                    textTransactions.setText("Transactions: " + transactions.size());
                } else {
                    textTransactions.setText("Transactions: 0");
                }
            });

        } else {
            textActiveUsers.setVisibility(View.GONE);
            inventoryViewModel.getUserTransactions().observe(getViewLifecycleOwner(), transactions -> {
                if (transactions != null) {
                    textTransactions.setText("Transactions: " + transactions.size());
                } else {
                    textTransactions.setText("Transactions: 0");
                }
            });
        }
        inventoryViewModel.getItems().observe(getViewLifecycleOwner(), items -> {
            if (items != null) {
                textItemsCount.setText("Items: " + items.size());
            } else {
                textItemsCount.setText("Items: 0");
            }
        });

        // Observe the timestamp from ViewModel
        inventoryViewModel.getExp().observe(getViewLifecycleOwner(), timestamp -> {
            if (countDownTimer != null) {
                countDownTimer.cancel(); // Cancel the previous countdown if it exists
            }

            ZonedDateTime targetTime = ZonedDateTime.ofInstant(Instant.ofEpochSecond(Long.parseLong(timestamp)), ZoneOffset.UTC);
            long deltaInMillis = Duration.between(ZonedDateTime.now(ZoneOffset.UTC), targetTime).toMillis();

            countDownTimer = new CountDownTimer(deltaInMillis, 1000) {
                public void onTick(long millisUntilFinished) {
                    int minutes = (int) (millisUntilFinished / 1000) / 60;
                    int seconds = (int) (millisUntilFinished / 1000) % 60;
                    textTokenCountdown.setText("Token expires in: " + minutes + ":" + String.format("%02d", seconds));
                }

                public void onFinish() {
                    textTokenCountdown.setText("Token expired");
                }
            };

            countDownTimer.start(); // Start the new countdown
        });

        // Button to refresh token
        buttonRefreshToken.setOnClickListener(v -> {
            // Refresh token and immediately fetch expiration timestamp
            inventoryViewModel.refreshToken().observe(getViewLifecycleOwner(), success -> {
                if (success) {
                    inventoryViewModel.fetchExp();
                }
            });

        });
    }

    private void startCountdown(ZonedDateTime targetTime) {
        // Stop any previously running countdown
        if (countdownRunnable != null) {
            countdownHandler.removeCallbacks(countdownRunnable);
        }

        // Define the new countdownRunnable
        countdownRunnable = new Runnable() {
            @Override
            public void run() {
                // Calculate the time remaining until the target time
                long deltaInSeconds = Duration.between(ZonedDateTime.now(ZoneOffset.UTC), targetTime).getSeconds();

                // If target time is in the future, calculate the countdown
                if (deltaInSeconds > 0) {
                    long minutes = deltaInSeconds / 60;
                    long seconds = deltaInSeconds % 60;
                    String delta = String.format("%02d:%02d", minutes, seconds);
                    textTokenCountdown.setText(delta);
                } else {
                    // If the countdown is finished, display 00:00 or a different message
                    textTokenCountdown.setText("00:00");
                }

                // Keep updating the countdown every second
                countdownHandler.postDelayed(this, 1000);
            }
        };

        // Start the countdown immediately
        countdownHandler.post(countdownRunnable);
    }

    // Method to stop the countdown when refreshing the token or for other reasons
    private void stopCountdown() {
        if (countdownRunnable != null) {
            countdownHandler.removeCallbacks(countdownRunnable);
            countdownRunnable = null; // Reset the Runnable

        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Clean up the countdown handler when the fragment is destroyed
        if (countdownRunnable != null) {
            countdownHandler.removeCallbacks(countdownRunnable);
        }
        binding = null;
    }
}


