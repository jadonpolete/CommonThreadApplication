package com.example.commonthread.model;

/*
 * Comment model represents a single user comment on a post.
 * Each comment contains the text content and the username of the author.
 */
public class Comment {

    // The actual comment text entered by the user
    private String text;

    // The username of the user who created the comment
    private String author;

    /*
     * Constructs a new Comment object.
     *
     * @param text The content of the comment
     * @param author The username of the comment creator
     */
    public Comment(String text, String author) {
        this.text = text;
        this.author = author;
    }

    /*
     * Returns the comment text.
     */
    public String getText() {
        return text;
    }

    /*
     * Returns the author of the comment.
     */
    public String getAuthor() {
        return author;
    }
}