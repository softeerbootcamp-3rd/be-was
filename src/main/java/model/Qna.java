package model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Qna {
    private String writer;
    private String title;
    private String contents;
    private String createdAt;

    public Qna(String writer, String title, String contents, String createdAt) {
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.createdAt = createdAt;
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

    public String getCreatedAt() {
        return createdAt;
    }
}
