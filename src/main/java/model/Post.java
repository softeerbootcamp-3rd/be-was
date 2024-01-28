package model;

import config.Config;

public class Post {

    private Long id;
    private String userId;      // 글 작성자 아이디 (이후 글 작성자 확인용)
    private String writerName;      // 글 작성자가 입력한 글쓴이
    private String title;
    private String contents;

    public Post(String userId, String writerName, String title, String contents) {
        this.id = ++Config.postId;
        this.userId = userId;
        this.writerName = writerName;
        this.title = title;
        this.contents = contents;
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

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setContents(String contents) {
        this.contents = contents;
    }

    // 글 작성자와 아이디가 일치하는지 검증
    public boolean checkWriter(String userId) {
        return this.userId.equals(userId);
    }

}
