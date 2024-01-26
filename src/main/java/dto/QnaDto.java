package dto;

import annotation.NotEmpty;

public class QnaDto {
    @NotEmpty
    private String title;

    @NotEmpty
    private String content;

    public QnaDto() {}

    public String getTitle() {
        return this.title;
    }

    public String getContent() {
        return this.content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
