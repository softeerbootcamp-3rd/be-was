package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private Long postId;
    private String userId;
    private String writer;
    private String title;
    private String contents;

    private List<Comment> commentList;


    private LocalDateTime createdAt;

    public Post(String userId, String writer, String title, String contents) {
        this.userId = userId;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
    }

    public Post(Long postId, Post post) {
        this.postId = postId;
        this.writer = post.getWriter();
        this.title = post.getTitle();
        this.contents = post.getContents();
        this.createdAt = LocalDateTime.now();
        commentList = new ArrayList<>();
    }

    public Long getPostId() {
        return postId;
    }

    public String getUserId() { return userId; }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public LocalDateTime getCreatedAt() { return createdAt; }

    public List<Comment> getCommentList() { return commentList; }
}
