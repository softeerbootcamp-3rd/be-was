package model;

import java.util.Date;

public class Post {
    private String postId;
    private String writer;
    private String title;
    private String contents;
    private String date;
    private String attachedFileName;

    public Post(String postId, String writer, String title, String contents, String date, String attachedFileName) {
        this.postId = postId;
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.date = date;
        this.attachedFileName = attachedFileName;
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

    public String getDate() {
        return date;
    }

    public String getAttachedFileName() {
        return attachedFileName;
    }

    @Override
    public String toString() {
        return "Post[" +
                "postId='" + postId + '\'' +
                ", writer='" + writer + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", date=" + date +
                ", attachedFileName='" + attachedFileName + '\'' +
                ']';
    }
}
