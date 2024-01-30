package model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Post {
    private static Long sequence = 0L;
    private Long postId;
    private User writer;
    private String title;
    private String contents;
    private LocalDateTime created;
    private LocalDateTime updated;
    private List<Comment> comments;
    private Long commentId;

    public Post(User writer, String title, String contents) {
        this.postId = ++sequence;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.created = LocalDateTime.now();
        this.comments = new ArrayList<>();
        this.commentId = 0L;
    }

    public Long addComment(User writer, String body) {
        Comment comment = new Comment(postId, ++commentId, writer, body);
        this.comments.add(comment);
        return commentId;
    }

    public void update(String title, String contents) {
        this.title = title;
        this.contents = contents;
        updated = LocalDateTime.now();
    }

    @Override
    public String toString() {
        return "Post [writer=" + writer.getUserId() + ", title=" + title + ", created=" + created + "]";
    }

    public Long getPostId() {
        return postId;
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
}
