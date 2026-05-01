package com.example.commonthread.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.commonthread.model.Post;
import com.example.commonthread.repository.PostRepository;
import com.example.commonthread.repository.SessionManager;

/*
 * CreatePostViewModel handles the logic for creating a new post.
 *
 * It validates user input, applies a default category when needed,
 * creates the Post object, and sends it to the repository.
 *
 * This class represents the ViewModel layer in the MVVM architecture.
 */
public class CreatePostViewModel extends ViewModel {

    private final PostRepository postRepository = new PostRepository();

    // Result message observed by CreatePostActivity
    private final MutableLiveData<String> resultMessage = new MutableLiveData<>();

    /*
     * Exposes post creation result messages to the View.
     */
    public LiveData<String> getResultMessage() {
        return resultMessage;
    }

    /*
     * Validates input and creates a new post if all fields are valid.
     *
     * @param title The title entered by the user
     * @param description The post body entered by the user
     * @param category The selected category for the post
     */
    public void createPost(String title, String description, String category) {
        // Validate title
        if (title == null || title.trim().isEmpty()) {
            resultMessage.setValue("Post title is required.");
            return;
        }

        if (title.trim().length() < 3) {
            resultMessage.setValue("Post title must be at least 3 characters.");
            return;
        }

        // Validate description
        if (description == null || description.trim().isEmpty()) {
            resultMessage.setValue("Post description is required.");
            return;
        }

        if (description.trim().length() < 10) {
            resultMessage.setValue("Post description must be at least 10 characters.");
            return;
        }

        // Default category if no selection is available
        if (category == null || category.trim().isEmpty()) {
            category = "General";
        }

        // Use the logged-in username as the post author
        String username = SessionManager.getCurrentUsername();

        // Create and store the new post
        Post post = new Post(title.trim(), description.trim(), username, category.trim());
        postRepository.addPost(post);

        resultMessage.setValue("Post created successfully.");
    }
}