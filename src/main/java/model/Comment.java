package model;

import java.time.LocalDateTime;

public class Comment {
    private Long postId;
    private Long commentId;
    private User writer;
    private String body;
    private LocalDateTime created;

    public Comment(Long postId, Long commentId, User writer, String body) {
        this.postId = postId;
        this.commentId = commentId;
        this.writer = writer;
        this.body = body;
        this.created = LocalDateTime.now();
    }

    public Long getPostId() {
        return postId;
    }

    public Long getCommentId() {
        return commentId;
    }

    public User getWriter() {
        return writer;
    }

    public String getBody() {
        return body;
    }

    public LocalDateTime getCreated() {
        return created;
    }
}
