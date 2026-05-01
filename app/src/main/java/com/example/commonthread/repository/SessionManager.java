package com.example.commonthread.repository;

import com.example.commonthread.model.User;

/*
 * SessionManager maintains the current user's session during app runtime.
 *
 * It allows different parts of the application to access the logged-in user
 * without passing user data between activities.
 *
 * Note:
 * This session is stored in memory only and will reset when the app is closed.
 * A production application would use secure session handling or authentication tokens.
 */
public class SessionManager {

    // Holds the currently logged-in user
    private static User currentUser;

    /*
     * Logs in a user by storing their session.
     *
     * @param user The authenticated user
     */
    public static void login(User user) {
        currentUser = user;
    }

    /*
     * Logs out the current user by clearing the session.
     */
    public static void logout() {
        currentUser = null;
    }

    /*
     * Returns the currently logged-in user.
     */
    public static User getCurrentUser() {
        return currentUser;
    }

    /*
     * Returns the username of the current user.
     * If no user is logged in, returns "Guest".
     */
    public static String getCurrentUsername() {
        if (currentUser != null) {
            return currentUser.getFullName();
        }

        return "Guest";
    }
}