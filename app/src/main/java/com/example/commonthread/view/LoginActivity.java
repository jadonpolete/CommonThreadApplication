package com.example.commonthread.view;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.commonthread.R;
import com.example.commonthread.viewmodel.LoginViewModel;

/*
 * LoginActivity displays the login screen for CommonThread.
 *
 * Users can enter their email and password to log in, or navigate
 * to the registration screen to create a new account.
 *
 * This class represents the View layer in the MVVM architecture.
 */
public class LoginActivity extends AppCompatActivity {

    private LoginViewModel loginViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize UI components
        EditText etEmail = findViewById(R.id.etEmail);
        EditText etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnGoToRegister = findViewById(R.id.btnGoToRegister);
        TextView tvResult = findViewById(R.id.tvResult);

        // Initialize ViewModel
        loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // Send login input to the ViewModel for validation/authentication
        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            loginViewModel.login(email, password);
        });

        // Navigate to Registration screen
        btnGoToRegister.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
            startActivity(intent);
        });

        // Observe login result and update UI based on success/failure
        loginViewModel.getLoginResult().observe(this, result -> {
            tvResult.setText(result);

            if (result.equals("Login successful. Welcome to CommonThread.")) {
                tvResult.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

                // Open feed after successful login
                Intent intent = new Intent(LoginActivity.this, FeedActivity.class);
                startActivity(intent);
            } else {
                tvResult.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
        });
    }
}