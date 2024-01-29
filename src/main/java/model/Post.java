package model;

import webserver.MultipartFile;

import java.util.List;

public class Post {

    private String title;
    private String content;
    private String authorId;

    public Post(String title, String content, String authorId) {
        this.title = title;
        this.content = content;
        this.authorId = authorId;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getAuthorId() {
        return authorId;
    }
}
