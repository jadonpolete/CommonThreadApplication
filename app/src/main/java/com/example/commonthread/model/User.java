package com.example.commonthread.model;

/*
 * User model represents a registered user in the application.
 * It stores basic authentication and identification data.
 *
 * Note: Passwords are stored in plain text for demo purposes only.
 * A production application would hash passwords before storing them.
 */
public class User {

    // User's display name (used in posts/comments)
    private String fullName;

    // User's email (used as unique identifier for login)
    private String email;

    // User's password (stored in plain text for demo purposes)
    private String password;

    // Confirmation password (used during registration validation)
    private String confirmPassword;

    /*
     * Constructs a new User object.
     *
     * @param fullName The user's display name
     * @param email The user's email (login identifier)
     * @param password The user's password
     * @param confirmPassword The confirmation password
     */
    public User(String fullName, String email, String password, String confirmPassword) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    /*
     * Returns the user's display name.
     */
    public String getFullName() {
        return fullName;
    }

    /*
     * Updates the user's display name.
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /*
     * Returns the user's email.
     */
    public String getEmail() {
        return email;
    }

    /*
     * Updates the user's email.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /*
     * Returns the user's password.
     */
    public String getPassword() {
        return password;
    }

    /*
     * Updates the user's password.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /*
     * Returns the confirmation password.
     */
    public String getConfirmPassword() {
        return confirmPassword;
    }

    /*
     * Updates the confirmation password.
     */
    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}