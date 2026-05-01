package com.example.commonthread.repository;

import com.example.commonthread.model.Comment;
import com.example.commonthread.model.Post;

import java.util.ArrayList;
import java.util.List;

/*
 * PostRepository simulates a data source for posts.
 *
 * It uses a static in-memory list to store posts, acting as a temporary database.
 * This allows posts, comments, and likes to persist during runtime.
 *
 * In a production application, this would be replaced with a database or backend service.
 */
public class PostRepository {

    // Static list to simulate persistent storage during app runtime
    private static final List<Post> posts = new ArrayList<>();

    /*
     * Constructor initializes sample posts only once.
     * Ensures demo data is available when the app starts.
     */
    public PostRepository() {
        if (posts.isEmpty()) {
            posts.add(new Post("Welcome to CommonThread", "This is the first discussion post.", "Admin", "General"));
            posts.add(new Post("Favorite Tech Topics", "What technology topics are you interested in?", "User One", "Tech"));
            posts.add(new Post("Project Demo Thread", "Use this post to test comments and interaction later.", "User Two", "School"));
        }
    }

    /*
     * Returns all posts.
     */
    public List<Post> getPosts() {
        return posts;
    }

    /*
     * Adds a new post to the top of the feed.
     *
     * @param post The post to be added
     */
    public void addPost(Post post) {
        // Insert at index 0 so newest posts appear first
        posts.add(0, post);
    }

    /*
     * Retrieves a post by its index in the list.
     *
     * @param index The position of the post
     * @return The Post if index is valid, otherwise null
     */
    public Post getPostByIndex(int index) {
        if (index >= 0 && index < posts.size()) {
            return posts.get(index);
        }

        return null;
    }

    /*
     * Adds a comment to a specific post.
     *
     * @param postIndex The index of the post
     * @param commentText The comment text
     * @return true if successful, false if post not found
     */
    public boolean addCommentToPost(int postIndex, String commentText) {
        Post post = getPostByIndex(postIndex);

        if (post == null) {
            return false;
        }

        // Retrieve current logged-in user
        String username = SessionManager.getCurrentUsername();

        // Add new comment to post
        post.addComment(new Comment(commentText, username));
        return true;
    }

    /*
     * Adds a like to a post.
     *
     * @param postIndex The index of the post
     * @param username The user liking the post
     * @return true if like was added, false otherwise
     */
    public boolean likePost(int postIndex, String username) {
        Post post = getPostByIndex(postIndex);

        if (post == null) {
            return false;
        }

        return post.addLike(username);
    }

    /*
     * Removes a like from a post.
     *
     * @param postIndex The index of the post
     * @param username The user removing their like
     * @return true if like was removed, false otherwise
     */
    public boolean unlikePost(int postIndex, String username) {
        Post post = getPostByIndex(postIndex);

        if (post == null) {
            return false;
        }

        return post.removeLike(username);
    }
}