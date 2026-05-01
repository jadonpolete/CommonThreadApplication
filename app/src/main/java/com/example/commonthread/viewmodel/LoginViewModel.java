package com.example.commonthread.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.commonthread.model.User;
import com.example.commonthread.repository.AuthRepository;
import com.example.commonthread.repository.SessionManager;

/*
 * LoginViewModel handles authentication logic for the login screen.
 *
 * It validates user input, normalizes email format, verifies credentials
 * using the repository, and updates the session on successful login.
 *
 * This class represents the ViewModel layer in the MVVM architecture.
 */
public class LoginViewModel extends ViewModel {

    private final AuthRepository repository = new AuthRepository();

    // LiveData observed by LoginActivity to display login result
    private final MutableLiveData<String> loginResult = new MutableLiveData<>();

    /*
     * Exposes login result messages to the View.
     */
    public LiveData<String> getLoginResult() {
        return loginResult;
    }

    /*
     * Attempts to authenticate the user.
     *
     * @param email The user's email
     * @param password The user's password
     */
    public void login(String email, String password) {

        // Validate input fields
        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            loginResult.setValue("Email and password cannot be empty.");
            return;
        }

        // Normalize email to ensure consistent matching (case-insensitive)
        email = email.trim().toLowerCase();

        // Attempt to retrieve user from repository
        User user = repository.getUser(email, password);

        if (user != null) {
            // Store logged-in user in session
            SessionManager.login(user);

            loginResult.setValue("Login successful. Welcome to CommonThread.");
        } else {
            // Invalid credentials
            loginResult.setValue("Invalid email or password.");
        }
    }
}