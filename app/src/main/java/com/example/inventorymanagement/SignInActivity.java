package com.example.inventorymanagement;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.inventorymanagement.databinding.ActivitySignInBinding;
import com.example.inventorymanagementsdk.InventoryManagement;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Response;

public class SignInActivity extends AppCompatActivity {
    private TextInputEditText usernameEditText;
    private TextInputEditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private SwitchCompat roleSwitch;

    private ActivitySignInBinding binding;
    private InventoryManagement inventoryManagement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
        setUpListeners();
    }

    public void initViews() {
        usernameEditText = binding.username;
        passwordEditText = binding.password;
        loginButton = binding.loginButton;
        registerButton = binding.registerButton;
        roleSwitch = binding.role;
        inventoryManagement = new InventoryManagement(getApplicationContext());
    }

    public void setUpListeners() {
        loginButton.setOnClickListener(v -> {
            String username = Objects.requireNonNull(usernameEditText.getText()).toString();
            String password = Objects.requireNonNull(passwordEditText.getText()).toString();
            inventoryManagement.login(username, password).observe(this, success -> {
                if (success!=null) {
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    new AlertDialog.Builder(SignInActivity.this)
                            .setTitle("Login Failed")
                            .setMessage("Invalid username or password")
                            .setPositiveButton("OK", null)
                            .show();
                }
            });
        });

        registerButton.setOnClickListener(v -> {
            String username = Objects.requireNonNull(usernameEditText.getText()).toString();
            String password = Objects.requireNonNull(passwordEditText.getText()).toString();
            String role = roleSwitch.isChecked() ? "manager" : "user";
            inventoryManagement.register(username, password, role).observe(this, success -> {
                if (success!=null) {
                    new AlertDialog.Builder(SignInActivity.this)
                            .setTitle("Registration Successful")
                            .setMessage("You can now login")
                            .setPositiveButton("OK", null)
                            .show();
                } else {
                    new AlertDialog.Builder(SignInActivity.this)
                            .setTitle("Registration Failed")
                            .setMessage("Username already exists")
                            .setPositiveButton("OK", null)
                            .show();
                }
            });

        });

    }

}