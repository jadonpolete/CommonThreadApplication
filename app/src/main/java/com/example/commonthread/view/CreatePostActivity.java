package com.example.commonthread.view;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.commonthread.R;
import com.example.commonthread.viewmodel.CreatePostViewModel;

/*
 * CreatePostActivity provides the UI for users to create a new post.
 *
 * It collects user input (title, description, category),
 * sends it to the ViewModel for validation and processing,
 * and displays success or error messages.
 *
 * This class represents the View layer in the MVVM architecture.
 */
public class CreatePostActivity extends AppCompatActivity {

    private CreatePostViewModel createPostViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_post);

        // Initialize UI components
        EditText etPostTitle = findViewById(R.id.etPostTitle);
        EditText etPostDescription = findViewById(R.id.etPostDescription);
        Spinner spinnerCategory = findViewById(R.id.spinnerCategory);
        Button btnSubmitPost = findViewById(R.id.btnSubmitPost);
        Button btnBackToFeed = findViewById(R.id.btnBackToFeed);
        TextView tvCreatePostResult = findViewById(R.id.tvCreatePostResult);

        // Initialize ViewModel
        createPostViewModel = new ViewModelProvider(this).get(CreatePostViewModel.class);

        // Configure category dropdown (simulates community grouping)
        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                new String[]{"General", "Tech", "School"}
        );
        categoryAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerCategory.setAdapter(categoryAdapter);

        // Handle post submission
        btnSubmitPost.setOnClickListener(v -> {
            String title = etPostTitle.getText().toString();
            String description = etPostDescription.getText().toString();

            // Default category is General if none selected
            String category = "General";
            if (spinnerCategory.getSelectedItem() != null) {
                category = spinnerCategory.getSelectedItem().toString();
            }

            // Send input to ViewModel for validation and creation
            createPostViewModel.createPost(title, description, category);
        });

        // Return to feed screen
        btnBackToFeed.setOnClickListener(v -> finish());

        // Observe result messages from ViewModel
        createPostViewModel.getResultMessage().observe(this, message -> {
            tvCreatePostResult.setText(message);

            if (message.equals("Post created successfully.")) {
                // Display success message in green
                tvCreatePostResult.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

                // Clear input fields after successful post
                etPostTitle.setText("");
                etPostDescription.setText("");

                // Reset category to default
                spinnerCategory.setSelection(0);
            } else {
                // Display error messages in red
                tvCreatePostResult.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
        });
    }
}