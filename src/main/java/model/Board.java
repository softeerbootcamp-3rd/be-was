package model;

import annotation.Column;

import java.time.LocalDate;

public class Board {
    private Integer id;
    @Column
    private String writer;
    @Column
    private String title;
    @Column
    private String content;
    @Column
    private String fileUpload;
    private LocalDate createdAt;

    public Board() {
    }

    public Board(Integer id, String writer, String title, String content, LocalDate createdAt, String fileUpload) {
        this.id = id;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.createdAt = createdAt;
        this.fileUpload = fileUpload;
    }

    public Integer getId() {
        return id;
    }

    public String getWriter() {
        return writer;
    }


    public String getTitle() {
        return title;
    }


    public String getContent() {
        return content;
    }

    public LocalDate getCreatedDate() {
        return createdAt;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public String getFileUpload() {
        return fileUpload;
    }

    @Override
    public String toString() {
        return "Board [writer=" + writer + ", title=" + title + ", content=" + content + ", createdAt="
                + createdAt + "]";
    }
}
