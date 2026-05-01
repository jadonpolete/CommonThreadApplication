package com.example.commonthread.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.commonthread.model.Post;
import com.example.commonthread.repository.PostRepository;
import com.example.commonthread.repository.SessionManager;

import java.util.ArrayList;
import java.util.List;

/*
 * FeedViewModel handles the business logic for the feed screen.
 *
 * It loads posts from the repository, applies sorting/filtering,
 * handles like/unlike behavior, and exposes post data to FeedActivity.
 *
 * This class represents the ViewModel layer in the MVVM architecture.
 */
public class FeedViewModel extends ViewModel {

    private final PostRepository postRepository = new PostRepository();

    // LiveData list observed by FeedActivity
    private final MutableLiveData<List<Post>> posts = new MutableLiveData<>();

    // Default feed option
    private String currentSort = "Newest";

    /*
     * Exposes posts to the View.
     */
    public LiveData<List<Post>> getPosts() {
        return posts;
    }

    /*
     * Loads posts from the repository and applies the current
     * sorting or filtering option selected by the user.
     */
    public void loadPosts() {
        List<Post> allPosts = new ArrayList<>(postRepository.getPosts());
        List<Post> displayedPosts = new ArrayList<>();

        if (currentSort.equals("Most Liked")) {
            // Sort posts by highest number of likes
            allPosts.sort((p1, p2) -> Integer.compare(p2.getLikes(), p1.getLikes()));
            displayedPosts = allPosts;

        } else if (currentSort.equals("Most Discussed")) {
            // Sort posts by highest number of comments
            allPosts.sort((p1, p2) -> Integer.compare(p2.getComments().size(), p1.getComments().size()));
            displayedPosts = allPosts;

        } else if (currentSort.equals("Tech") || currentSort.equals("School") || currentSort.equals("General")) {
            // Filter posts by selected category
            for (Post post : allPosts) {
                if (post.getCategory().equals(currentSort)) {
                    displayedPosts.add(post);
                }
            }

        } else {
            // Default behavior: newest posts appear first
            displayedPosts = allPosts;
        }

        posts.setValue(displayedPosts);
    }

    /*
     * Updates the current sort/filter option and reloads the feed.
     *
     * @param sortOption The selected sort/filter option
     */
    public void setSort(String sortOption) {
        currentSort = sortOption;
        loadPosts();
    }

    /*
     * Toggles like/unlike for the selected post.
     *
     * Because the displayed feed may be sorted or filtered, the method first
     * maps the displayed post back to its real index in the repository.
     *
     * @param postIndex The index of the post in the currently displayed feed
     */
    public void toggleLikePost(int postIndex) {
        String username = SessionManager.getCurrentUsername();

        // Get selected post from currently displayed list
        Post selectedPost = posts.getValue().get(postIndex);

        // Find the same post in the repository's full list
        int realIndex = postRepository.getPosts().indexOf(selectedPost);

        Post post = postRepository.getPostByIndex(realIndex);

        if (post == null) {
            return;
        }

        // Toggle like state based on whether user already liked the post
        if (post.hasUserLiked(username)) {
            postRepository.unlikePost(realIndex, username);
        } else {
            postRepository.likePost(realIndex, username);
        }

        // Refresh feed after update
        loadPosts();
    }
}