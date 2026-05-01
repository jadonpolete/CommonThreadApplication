package com.example.commonthread.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.commonthread.model.User;
import com.example.commonthread.repository.AuthRepository;

/*
 * RegistrationViewModel handles user registration logic.
 *
 * It validates user input, normalizes email formatting,
 * communicates with the repository to create a new user,
 * and provides status updates to the View.
 *
 * This class represents the ViewModel layer in the MVVM architecture.
 */
public class RegistrationViewModel extends ViewModel {

    /*
     * Enum representing the current registration state.
     * Used by the View to update UI behavior (e.g., loading, success, error).
     */
    public enum RegistrationState {
        IDLE, LOADING, SUCCESS, ERROR
    }

    private final AuthRepository repository = new AuthRepository();

    // LiveData for tracking registration state
    private final MutableLiveData<RegistrationState> registrationState =
            new MutableLiveData<>(RegistrationState.IDLE);

    // LiveData for error messages
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();

    // LiveData for success messages
    private final MutableLiveData<String> successMessage = new MutableLiveData<>();

    /*
     * Exposes registration state to the View.
     */
    public LiveData<RegistrationState> getRegistrationState() {
        return registrationState;
    }

    /*
     * Exposes error messages to the View.
     */
    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /*
     * Exposes success messages to the View.
     */
    public LiveData<String> getSuccessMessage() {
        return successMessage;
    }

    /*
     * Validates user input and attempts to register a new user.
     *
     * @param fullName The user's display name
     * @param email The user's email (used as login identifier)
     * @param password The user's password
     * @param confirmPassword Confirmation of the password
     */
    public void register(String fullName, String email, String password, String confirmPassword) {

        // Set loading state while processing registration
        registrationState.setValue(RegistrationState.LOADING);

        // Validate username
        if (fullName == null || fullName.trim().isEmpty()) {
            errorMessage.setValue("Username is required");
            registrationState.setValue(RegistrationState.ERROR);
            return;
        }

        if (fullName.trim().length() < 3) {
            errorMessage.setValue("Username must be at least 3 characters");
            registrationState.setValue(RegistrationState.ERROR);
            return;
        }

        // Validate email
        if (email == null || email.trim().isEmpty()) {
            errorMessage.setValue("Email is required");
            registrationState.setValue(RegistrationState.ERROR);
            return;
        }

        // Validate password
        if (password == null || password.length() < 6) {
            errorMessage.setValue("Password must be at least 6 characters");
            registrationState.setValue(RegistrationState.ERROR);
            return;
        }

        // Require at least one number
        if (!password.matches(".*\\d.*")) {
            errorMessage.setValue("Password must contain at least one number");
            registrationState.setValue(RegistrationState.ERROR);
            return;
        }

        // Validate password confirmation
        if (!password.equals(confirmPassword)) {
            errorMessage.setValue("Passwords do not match");
            registrationState.setValue(RegistrationState.ERROR);
            return;
        }

        // Normalize email to ensure case-insensitive uniqueness
        email = email.trim().toLowerCase();

        // Create user object
        User user = new User(fullName.trim(), email, password, confirmPassword);

        // Attempt registration through repository
        boolean success = repository.registerUser(user);

        if (success) {
            successMessage.setValue("Registration successful.");
            registrationState.setValue(RegistrationState.SUCCESS);
        } else {
            errorMessage.setValue("User already exists.");
            registrationState.setValue(RegistrationState.ERROR);
        }
    }
}