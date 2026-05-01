package com.example.commonthread.view;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.commonthread.R;
import com.example.commonthread.model.Comment;
import com.example.commonthread.viewmodel.PostDetailViewModel;

/*
 * PostDetailActivity displays a single selected post.
 *
 * Users can view the post content, read existing comments,
 * add a new comment, or return to the feed.
 *
 * This class represents the View layer in the MVVM architecture.
 */
public class PostDetailActivity extends AppCompatActivity {

    private PostDetailViewModel postDetailViewModel;
    private int postIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // Initialize UI components
        TextView tvPostTitle = findViewById(R.id.tvPostTitle);
        TextView tvPostAuthor = findViewById(R.id.tvPostAuthor);
        TextView tvPostDescription = findViewById(R.id.tvPostDescription);
        TextView tvComments = findViewById(R.id.tvComments);
        EditText etComment = findViewById(R.id.etComment);
        Button btnAddComment = findViewById(R.id.btnAddComment);
        Button btnBackToFeed = findViewById(R.id.btnBackToFeed);
        TextView tvCommentResult = findViewById(R.id.tvCommentResult);

        // Retrieve selected post index from FeedActivity
        postIndex = getIntent().getIntExtra("postIndex", -1);

        // Initialize ViewModel and load selected post
        postDetailViewModel = new ViewModelProvider(this).get(PostDetailViewModel.class);
        postDetailViewModel.loadPost(postIndex);

        // Observe selected post and update UI when post data changes
        postDetailViewModel.getSelectedPost().observe(this, post -> {
            if (post == null) {
                tvPostTitle.setText("Post not found.");
                return;
            }

            tvPostTitle.setText(post.getTitle());
            tvPostAuthor.setText("Author: " + post.getAuthor());
            tvPostDescription.setText(post.getDescription());

            // Display comments or fallback message
            if (post.getComments().isEmpty()) {
                tvComments.setText("No comments yet.");
            } else {
                StringBuilder commentText = new StringBuilder();

                for (Comment comment : post.getComments()) {
                    commentText.append(comment.getAuthor())
                            .append(": ")
                            .append(comment.getText())
                            .append("\n\n");
                }

                tvComments.setText(commentText.toString());
            }
        });

        // Send new comment input to ViewModel
        btnAddComment.setOnClickListener(v -> {
            String comment = etComment.getText().toString();
            postDetailViewModel.addComment(postIndex, comment);
            etComment.setText("");
        });

        // Return to feed
        btnBackToFeed.setOnClickListener(v -> finish());

        // Observe comment result messages
        postDetailViewModel.getResultMessage().observe(this, message -> {
            tvCommentResult.setText(message);

            if (message.equals("Comment added successfully.")) {
                tvCommentResult.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
            } else {
                tvCommentResult.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
            }
        });
    }
}