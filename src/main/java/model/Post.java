package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private User writer;
    private String title;
    private String contents;
    private List<Comment> comments;
    private LocalDateTime created;

    public Post(User writer, String title, String contents) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.comments = new ArrayList<>();
        this.created = LocalDateTime.now();
    }

    public User getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public List<Comment> getComments() {
        return comments;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    @Override
    public String toString() {
        return "Post [writer=" + writer.getUserId() + ", title=" + title + ", created=" + created + "]";
    }
}
