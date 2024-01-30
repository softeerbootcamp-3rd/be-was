package model;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

public class Post {
    private int postId;
    private String writer;
    private String title;
    private String contents;
    private String fileId;
    private String fileExtension;

    private Timestamp createdAt;

    private int commentCount;

    public Post(int postId, String writer, String title, String contents, String fileId, String fileExtension, Timestamp createdAt) {
        this.postId = postId;
        this.writer = writer;
        this.title = title;
        this.fileId = fileId;
        this.fileExtension = fileExtension;
        this.contents = contents;
        this.createdAt = createdAt;
        this.commentCount = 0;
    }

    public Post(String writer, String title, String contents, String fileId, String fileExtension, Timestamp createdAt) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.fileId = fileId;
        this.fileExtension = fileExtension;
        this.createdAt = createdAt;
        this.commentCount = 0;
    }

    public int getPostId() {
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

    public String getFileId() {
        return fileId;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public String getCreatedAt() {
        Timestamp timestamp = createdAt;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return sdf.format(timestamp);
    }

    public int getCommentCount() {
        return commentCount;
    }

    @Override
    public String toString() {
        return "Post{" +
                "writer='" + writer + '\'' +
                ", title='" + title + '\'' +
                ", contents='" + contents + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
