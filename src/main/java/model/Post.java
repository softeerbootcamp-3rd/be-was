package model;

import java.time.LocalDateTime;

public class Post {
    private String writer;
    private String title;
    private String contents;
    private LocalDateTime createTime;

    public Post(String writer, String title, String contents) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.createTime = LocalDateTime.now(); // 현재 시간으로 초기화
    }

    // Getter 메소드들
    public String getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    // createTime에 대한 getter 메서드
    public LocalDateTime getCreateTime() {
        return createTime;
    }
}
