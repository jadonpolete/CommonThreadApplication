package com.example.commonthread.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.commonthread.model.Post;
import com.example.commonthread.repository.PostRepository;

/*
 * PostDetailViewModel manages the logic for a single post detail screen.
 *
 * It loads the selected post, handles comment validation and submission,
 * and exposes both post data and result messages to the View.
 *
 * This class represents the ViewModel layer in the MVVM architecture.
 */
public class PostDetailViewModel extends ViewModel {

    private final PostRepository postRepository = new PostRepository();

    // LiveData for the currently selected post
    private final MutableLiveData<Post> selectedPost = new MutableLiveData<>();

    // LiveData for displaying result messages (success/error)
    private final MutableLiveData<String> resultMessage = new MutableLiveData<>();

    /*
     * Exposes the selected post to the View.
     */
    public LiveData<Post> getSelectedPost() {
        return selectedPost;
    }

    /*
     * Exposes comment result messages to the View.
     */
    public LiveData<String> getResultMessage() {
        return resultMessage;
    }

    /*
     * Loads a post based on its index and updates LiveData.
     *
     * @param postIndex The index of the selected post
     */
    public void loadPost(int postIndex) {
        Post post = postRepository.getPostByIndex(postIndex);
        selectedPost.setValue(post);
    }

    /*
     * Validates and adds a comment to the selected post.
     *
     * @param postIndex The index of the post
     * @param commentText The text entered by the user
     */
    public void addComment(int postIndex, String commentText) {
        // Validate comment input
        if (commentText == null || commentText.trim().isEmpty()) {
            resultMessage.setValue("Comment cannot be empty.");
            return;
        }

        if (commentText.trim().length() < 2) {
            resultMessage.setValue("Comment must be at least 2 characters.");
            return;
        }

        // Attempt to add comment through repository
        boolean success = postRepository.addCommentToPost(postIndex, commentText.trim());

        if (success) {
            resultMessage.setValue("Comment added successfully.");

            // Reload post to reflect new comment in UI
            loadPost(postIndex);
        } else {
            resultMessage.setValue("Unable to add comment.");
        }
    }
}