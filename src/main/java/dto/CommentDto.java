package dto;

import annotation.NotEmpty;

public class CommentDto {

    @NotEmpty
    private String content;

    public CommentDto() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
