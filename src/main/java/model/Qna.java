package model;

import dto.QnaDto;
import org.checkerframework.checker.units.qual.A;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Qna {
    Long Id;



    User writer;
    String title;
    List<String> contents = new ArrayList<>();
    LocalDateTime createAt = LocalDateTime.now();;

    public Qna() {
    }

    public Qna(QnaDto qnaDto){
        this.title = qnaDto.getTitle();
        this.contents = Arrays.asList(qnaDto.getContents().split("\\r?\\n"));

    }

    public LocalDateTime getCreateAt() {
        return createAt;
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

    public List<String> getContent() {
        return contents;
    }

    public void setContent(String content) {
        this.contents = Arrays.asList(content.split("\\r?\\n"));;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }
}
