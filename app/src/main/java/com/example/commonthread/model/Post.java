package com.example.commonthread.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/*
 * Post model represents a discussion post in the application.
 * A post contains content, metadata (author, category, timestamp),
 * and interaction data such as likes and comments.
 */
public class Post {

    // Basic post information
    private String title;
    private String description;
    private String author;
    private String category;
    private String timestamp;

    // Like tracking
    private int likes;
    private Set<String> likedByUsers; // Tracks users who have liked this post

    // List of comments associated with the post
    private List<Comment> comments;

    /*
     * Constructs a new Post object with initial values.
     *
     * @param title The title of the post
     * @param description The body/content of the post
     * @param author The username of the post creator
     * @param category The category/tag assigned to the post
     */
    public Post(String title, String description, String author, String category) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.category = category;

        // Generate a formatted timestamp when the post is created
        this.timestamp = new SimpleDateFormat("MMM d, yyyy h:mm a", Locale.getDefault()).format(new Date());

        // Initialize interaction data
        this.likes = 0;
        this.likedByUsers = new HashSet<>();
        this.comments = new ArrayList<>();
    }

    /*
     * Returns the post title.
     */
    public String getTitle() {
        return title;
    }

    /*
     * Returns the post description/content.
     */
    public String getDescription() {
        return description;
    }

    /*
     * Returns the username of the author.
     */
    public String getAuthor() {
        return author;
    }

    /*
     * Returns the category of the post.
     */
    public String getCategory() {
        return category;
    }

    /*
     * Returns the timestamp of when the post was created.
     */
    public String getTimestamp() {
        return timestamp;
    }

    /*
     * Returns the total number of likes.
     */
    public int getLikes() {
        return likes;
    }

    /*
     * Adds a like from a user.
     * Prevents duplicate likes from the same user.
     *
     * @param username The user attempting to like the post
     * @return true if like was added, false if user already liked
     */
    public boolean addLike(String username) {
        if (likedByUsers.contains(username)) {
            return false;
        }

        likedByUsers.add(username);
        likes++;
        return true;
    }

    /*
     * Removes a like from a user.
     *
     * @param username The user removing their like
     * @return true if like was removed, false if user had not liked
     */
    public boolean removeLike(String username) {
        if (!likedByUsers.contains(username)) {
            return false;
        }

        likedByUsers.remove(username);
        likes--;
        return true;
    }

    /*
     * Checks if a user has already liked this post.
     */
    public boolean hasUserLiked(String username) {
        return likedByUsers.contains(username);
    }

    /*
     * Returns the list of comments on the post.
     */
    public List<Comment> getComments() {
        return comments;
    }

    /*
     * Adds a new comment to the post.
     */
    public void addComment(Comment comment) {
        comments.add(comment);
    }
}