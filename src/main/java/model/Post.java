package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Post {

    private final long postId;
    private final String writer;
    private final String title;
    private final String contents;
    private final LocalDateTime postTime;
    private final int reply;
    private final String imageName;

    public Post(long postId, String writer, String title, String contents, LocalDateTime postTime, String imageName) {
        this.postId = postId;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.postTime = postTime;
        this.reply = 0;
        this.imageName = imageName;
    }

    public long getPostId() {
        return postId;
    }

    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public String getPostTime() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd hh:mm");
        return postTime.format(formatter);
    }

    public int getReply() {
        return reply;
    }

    public String getImageName() {
        return imageName;
    }

    @Override
    public String toString() {
        return "Post [writer=" + writer + ", title=" + title + ", contents=" + contents + ", postTime=" + postTime + "]";
    }
}
