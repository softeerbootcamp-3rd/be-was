package dto;

import annotation.NotEmpty;

public class QnaAnswerDto {

    @NotEmpty
    private String content;

    public QnaAnswerDto() {
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
