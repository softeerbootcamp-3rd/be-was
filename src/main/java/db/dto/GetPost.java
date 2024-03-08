package db.dto;

import java.time.LocalDateTime;

public class GetPost {
    private int id;
    private String writer;
    private String title;
    private String content;

    private LocalDateTime createdTime;

    private int commentCount;

    public int getId(){return id;}
    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }
    public int getCommentCount() {
        return commentCount;
    }

    public LocalDateTime getCreatedTime() {
        return createdTime;
    }


    public GetPost(int id, String writer, String title, String content, LocalDateTime createdTime, int commentCount) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.createdTime = createdTime;
        this.commentCount = commentCount;
    }
}
