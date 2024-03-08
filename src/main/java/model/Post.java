package model;

import java.time.LocalDateTime;

public class Post {
    private String writer;
    private String title;
    private String content;

    private LocalDateTime createdTime;
    private int commentCount;

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }

    public int getCommentCount() {
        return commentCount;
    }


    public Post(String writer, String title, String content, LocalDateTime createdTime, int commentCount) {
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.createdTime = createdTime;
        this.commentCount = commentCount;
    }
}
