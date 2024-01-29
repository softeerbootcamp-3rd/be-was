package model;

import java.util.Date;

public class Post {
    private String postId;
    private String writer;
    private String title;
    private String contents;
    private Date date;

    public Post(String postId, String writer, String title, String contents, Date date) {
        this.postId = postId;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.date = date;
    }

    public String getPostId() {
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

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Post [postId=" + postId + ", writer=" + writer + ", title=" + title + ", contents=" + contents + ", date=" + date + ']';
    }
}
