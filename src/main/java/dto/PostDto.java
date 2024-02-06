package dto;

import annotation.NotEmpty;
import util.mapper.MultipartFile;

public class PostDto {
    @NotEmpty
    private String title;
    @NotEmpty
    private String contents;
    private MultipartFile attachment;

    public PostDto() {}

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

    public MultipartFile getFile() {
        return attachment;
    }

    public void setAttachment(MultipartFile attachment) {
        this.attachment = attachment;
    }
}
