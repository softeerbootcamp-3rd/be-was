package model;

import config.Config;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Post implements Comparable<Post>{

    private Long id;
    private String userId;      // 글 작성자 아이디 (이후 글 작성자 확인용)
    private String writerName;      // 글 작성자가 입력한 글쓴이
    private String title;
    private String contents;
    private LocalDateTime createdAt;

    public Post(Long id, String userId, String writerName, String title, String contents) {
        this.id = id;
        this.userId = userId;
        this.writerName = writerName;
        this.title = title;
        this.contents = contents;
        this.createdAt = LocalDateTime.now();
    }

    public Post(String userId, String writerName, String title, String contents) {
        this.id = ++Config.postId;
        this.userId = userId;
        this.writerName = writerName;
        this.title = title;
        this.contents = contents;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }
    public String getUserId() {
        return userId;
    }
    public String getWriterName() {
        return writerName;
    }
    public String getTitle() {
        return title;
    }
    public String getContents() {
        return contents;
    }
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public String getCreateAtWithFormat() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return createdAt.format(formatter);
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setContents(String contents) {
        this.contents = contents;
    }
    public void setCreatedAt() {
        this.createdAt = LocalDateTime.now();
    }

    // 글 작성자와 아이디가 일치하는지 검증
    public boolean checkWriter(String userId) {
        return this.userId.equals(userId);
    }

    // 최신순 정렬
    @Override
    public int compareTo(Post p) {
        if(this.createdAt.isAfter(p.getCreatedAt()))
            return -1;
        else if(this.createdAt.isBefore(p.getCreatedAt()))
            return 1;
        else
            return 0;
    }
}
