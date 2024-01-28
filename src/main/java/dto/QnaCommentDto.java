package dto;

import annotation.NotEmpty;

public class QnaCommentDto {

    @NotEmpty
    private String content;

    public QnaCommentDto() {
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
