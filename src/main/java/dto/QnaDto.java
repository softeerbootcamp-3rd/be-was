package dto;

import annotation.NotEmpty;

public class QnaDto {
    @NotEmpty
    private String title;
    @NotEmpty
    private String contents;

    public QnaDto() {}

    public String getTitle() {
        return this.title;
    }

    public String getContents() {
        return this.contents;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
