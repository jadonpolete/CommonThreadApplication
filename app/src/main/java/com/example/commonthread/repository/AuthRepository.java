package com.example.commonthread.repository;

import com.example.commonthread.model.User;

import java.util.HashMap;
import java.util.Map;

/*
 * AuthRepository handles user registration and authentication logic.
 *
 * It uses an in-memory data structure (HashMap) to simulate a database.
 *
 * Demo security note:
 * Passwords are stored in plain text for simplicity.
 * In a real-world application, passwords should be hashed and stored securely,
 * and authentication would be handled through a backend service.
 */
public class AuthRepository {

    // Stores users with email as the unique key
    private static final Map<String, User> users = new HashMap<>();

    /*
     * Registers a new user if the email is not already in use.
     *
     * @param user The user attempting to register
     * @return true if registration is successful, false if email already exists
     */
    public boolean registerUser(User user) {
        // Check if email is already registered
        if (users.containsKey(user.getEmail())) {
            return false;
        }

        // Store new user in memory
        users.put(user.getEmail(), user);
        return true;
    }

    /*
     * Retrieves a user based on email and password.
     *
     * @param email The user's email
     * @param password The user's password
     * @return The matching User if credentials are valid, otherwise null
     */
    public User getUser(String email, String password) {
        User user = users.get(email);

        // Validate password
        if (user != null && user.getPassword().equals(password)) {
            return user;
        }

        return null;
    }
}