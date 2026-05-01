package com.example.commonthread.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.commonthread.R;
import com.example.commonthread.viewmodel.RegistrationViewModel;

/*
 * RegistrationActivity handles user account creation.
 *
 * Users enter their username, email, and password to register.
 * Input is validated through the ViewModel, and feedback is displayed
 * to the user based on success or failure.
 *
 * This class represents the View layer in the MVVM architecture.
 */
public class RegistrationActivity extends AppCompatActivity {

    private RegistrationViewModel registrationViewModel;

    // UI components
    private EditText etFullName;
    private EditText etEmail;
    private EditText etPassword;
    private EditText etConfirmPassword;
    private Button btnRegister;
    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        // Initialize UI components
        etFullName = findViewById(R.id.etFullName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnRegister = findViewById(R.id.btnRegister);
        tvResult = findViewById(R.id.tvResult);
        Button btnBackToLogin = findViewById(R.id.btnBackToLogin);

        // Initialize ViewModel
        registrationViewModel = new ViewModelProvider(this).get(RegistrationViewModel.class);

        // Handle registration button click
        btnRegister.setOnClickListener(v -> {
            String fullName = etFullName.getText().toString();
            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            // Send user input to ViewModel for validation and registration
            registrationViewModel.register(fullName, email, password, confirmPassword);
        });

        // Navigate back to Login screen
        btnBackToLogin.setOnClickListener(v -> {
            finish(); // Closes this activity and returns to LoginActivity
        });

        // Observe registration state to update button UI
        registrationViewModel.getRegistrationState().observe(this, state -> {
            switch (state) {
                case LOADING:
                    // Disable button while processing
                    btnRegister.setEnabled(false);
                    btnRegister.setText("Registering...");
                    break;

                case SUCCESS:
                case ERROR:
                case IDLE:
                    // Re-enable button after processing
                    btnRegister.setEnabled(true);
                    btnRegister.setText("Register");
                    break;
            }
        });

        // Observe error messages
        registrationViewModel.getErrorMessage().observe(this, error -> {
            if (error != null) {
                tvResult.setText(error);
                tvResult.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
        });

        // Observe success messages
        registrationViewModel.getSuccessMessage().observe(this, success -> {
            if (success != null) {
                tvResult.setText(success);
                tvResult.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            }
        });
    }
}