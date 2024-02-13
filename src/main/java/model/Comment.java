package model;

import java.time.LocalDateTime;

public class Comment {
    private Long commentId;
    private String userId;
    private String writer;
    private String content;
    private LocalDateTime createdAt;

    public Comment(String userId, String writer, String content) {
        this.userId = userId;
        this.writer = writer;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public String getUserId() { return userId; }

    public String getWriter() {
        return writer;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCommentId(Long commentId) {
        this.commentId = commentId;
    }
}
