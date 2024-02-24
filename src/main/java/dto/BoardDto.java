package dto;

import annotation.NotEmpty;
import model.MultipartFile;

public class BoardDto {
    @NotEmpty
    private String title;
    @NotEmpty
    private String contents;
    private MultipartFile attachment;

    public BoardDto() {}

    public String getTitle() {
        return title;
    }

    public String getContents() {
        return contents;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public MultipartFile getFile() {
        return attachment;
    }

    public void setAttachment(MultipartFile attachment) {
        this.attachment = attachment;
    }
}
