package com.example.commonthread.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.commonthread.R;
import com.example.commonthread.model.Post;
import com.example.commonthread.repository.SessionManager;
import com.example.commonthread.viewmodel.FeedViewModel;

/*
 * FeedActivity displays the main CommonThread feed.
 *
 * Users can view posts, sort/filter the feed, like/unlike posts,
 * open post details, create new posts, and log out.
 *
 * This class represents the View layer in the MVVM architecture.
 */
public class FeedActivity extends AppCompatActivity {

    private FeedViewModel feedViewModel;
    private LinearLayout postListContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // Initialize UI components
        Button btnCreatePost = findViewById(R.id.btnCreatePost);
        Button btnLogout = findViewById(R.id.btnLogout);
        Spinner spinnerSort = findViewById(R.id.spinnerSort);
        postListContainer = findViewById(R.id.postListContainer);
        TextView tvWelcomeUser = findViewById(R.id.tvWelcomeUser);

        // Display the currently logged-in username
        String username = SessionManager.getCurrentUsername();
        tvWelcomeUser.setText("Welcome, " + username + "!");

        // Initialize ViewModel
        feedViewModel = new ViewModelProvider(this).get(FeedViewModel.class);

        // Configure sorting/filtering dropdown
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                R.layout.spinner_item,
                new String[]{"Newest", "Most Liked", "Most Discussed", "Tech", "School", "General"}
        );
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerSort.setAdapter(adapter);

        // Send selected sorting/filtering option to the ViewModel
        spinnerSort.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, android.view.View view, int position, long id) {
                feedViewModel.setSort(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // No action needed
            }
        });

        // Navigate to Create Post screen
        btnCreatePost.setOnClickListener(v -> {
            Intent intent = new Intent(FeedActivity.this, CreatePostActivity.class);
            startActivity(intent);
        });

        // Log out user, clear session, and return to Login screen
        btnLogout.setOnClickListener(v -> {
            SessionManager.logout();

            Intent intent = new Intent(FeedActivity.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });

        // Observe posts from ViewModel and rebuild the feed when data changes
        feedViewModel.getPosts().observe(this, posts -> {
            postListContainer.removeAllViews();

            for (int i = 0; i < posts.size(); i++) {
                Post post = posts.get(i);
                int postIndex = i;
                int commentCount = post.getComments().size();

                // Create text display for a single post
                TextView postView = new TextView(this);
                postView.setText(
                        post.getTitle() + "\n" +
                                "Category: " + post.getCategory() + "\n" +
                                "Author: " + post.getAuthor() + "\n" +
                                "Posted: " + post.getTimestamp() + "\n" +
                                "Comments: " + commentCount + "   Likes: " + post.getLikes() + "\n\n" +
                                post.getDescription()
                );

                postView.setTextSize(16);
                postView.setTextColor(0xFFFFFFFF);
                postView.setPadding(0, 0, 0, 16);

                // Open selected post in detail screen
                postView.setOnClickListener(v -> {
                    Intent intent = new Intent(FeedActivity.this, PostDetailActivity.class);
                    intent.putExtra("postIndex", postIndex);
                    startActivity(intent);
                });

                // Create Like/Unlike button based on current user's like status
                Button likeButton = new Button(this);
                likeButton.setText(post.hasUserLiked(username) ? "Unlike" : "Like");
                likeButton.setTextColor(0xFFFFFFFF);
                likeButton.setTextSize(12);
                likeButton.setBackgroundResource(R.drawable.primary_button_background);

                // Toggle like state through ViewModel
                likeButton.setOnClickListener(v -> {
                    feedViewModel.toggleLikePost(postIndex);
                });

                // Align like button to the bottom-right of the card
                LinearLayout.LayoutParams likeParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                likeParams.gravity = Gravity.RIGHT;
                likeButton.setLayoutParams(likeParams);

                // Create card container for post content and like button
                LinearLayout cardLayout = new LinearLayout(this);
                cardLayout.setOrientation(LinearLayout.VERTICAL);
                cardLayout.setBackgroundResource(R.drawable.post_card_background);
                cardLayout.setPadding(24, 20, 24, 20);

                LinearLayout.LayoutParams cardParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                );
                cardParams.setMargins(0, 0, 0, 20);
                cardLayout.setLayoutParams(cardParams);

                // Add post content and like button to card
                cardLayout.addView(postView);
                cardLayout.addView(likeButton);

                // Add card to feed container
                postListContainer.addView(cardLayout);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Reload posts when returning from Create Post or Post Detail screens
        if (feedViewModel != null) {
            feedViewModel.loadPosts();
        }
    }
}