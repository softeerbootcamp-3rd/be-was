package dto;

import model.Post;

public class PostDtoBuilder {
    private String name;
    private String title;
    private String contents;
    private byte[] file;
    private String fileName;
    private String fileContentType;

    public PostDtoBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public PostDtoBuilder setTitle(String title) {
        this.title = title;
        return this;
    }

    public PostDtoBuilder setContents(String contents) {
        this.contents = contents;
        return this;
    }

    public PostDtoBuilder setFile(byte[] file) {
        this.file = file;
        return this;
    }

    public PostDtoBuilder setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public PostDtoBuilder setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
        return this;
    }

}
