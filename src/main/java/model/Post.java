package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Post {
    private String postId;
    private User writer;
    private String title;
    private String contents;
    private LocalDateTime createTime;

    public Post(User writer, String title, String contents) {
        this.postId = generatePostId();
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.createTime = LocalDateTime.now();
    }

    public String getPostId() {
        return this.postId;
    }

    public String getWriterName() {
        return this.writer.getName();
    }

    public String getUserId() {
        return this.writer.getUserId();
    }

    public String getTitle() {
        return this.title;
    }

    public String getContents() {
        return this.contents;
    }

    public String getCreateTime() {
        return this.createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    private String generatePostId() {
        return UUID.randomUUID().toString();
    }

}
