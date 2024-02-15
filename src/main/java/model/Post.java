package model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Post {
    private String postId;
    private User writer;
    private String title;
    private String contents;
    private LocalDateTime createTime;
    private byte[] file;
    private String fileName;
    private String fileContentType;

    public Post(User writer, String title, String contents, byte[] file, String fileName, String fileContentType) {
        this.postId = generatePostId();
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.createTime = LocalDateTime.now();
        this.file = file;
        this.fileName = fileName;
        this.fileContentType = fileContentType;
    }

    public Post(User writer, String title, String contents, byte[] file) {
        this.postId = generatePostId();
        this.writer = writer;
        this.title = title;
        this.contents = contents;
        this.createTime = LocalDateTime.now();
        this.file = file;
    }

    public String getPostId() {
        return this.postId;
    }

    public String getWriterName() {
        return this.writer.getName();
    }

    public String getUserId() {
        return this.writer.getUserId();
    }

    public String getTitle() {
        return this.title;
    }

    public String getContents() {
        return this.contents;
    }

    public String getCreateTime() {
        return this.createTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    private String generatePostId() {
        return UUID.randomUUID().toString();
    }

    public byte[] getFile() {
        return file;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileContentType() {
        return fileContentType;
    }

    public void setFileContentType(String fileContentType) {
        this.fileContentType = fileContentType;
    }
}
