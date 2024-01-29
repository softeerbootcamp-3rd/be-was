package model;

import dto.QnaDto;

import java.time.LocalDateTime;

public class Qna {
    Long Id;



    User writer;
    String title;
    String contents;
    LocalDateTime createAt;

    public Qna() {
    }

    public Qna(QnaDto qnaDto){
        this.title = qnaDto.getTitle();
        this.contents = qnaDto.getContents();
        createAt = LocalDateTime.now();
    }


    public Long getId() {
        return Id;
    }

    public void setId(Long id) {
        Id = id;
    }
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return contents;
    }

    public void setContent(String content) {
        this.contents = content;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }
}
