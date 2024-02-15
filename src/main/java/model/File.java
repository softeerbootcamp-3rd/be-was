package model;

public class File {

    private long postId;
    private String fileName;
    private String contentType;
    private byte[] fileContent;

    public File() {

    }

    public File(long postId, String fileName, String contentType, byte[] fileContent) {
        this.postId = postId;
        this.fileName = fileName;
        this.contentType = contentType;
        this.fileContent = fileContent;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public void setFileContent(byte[] fileContent) {
        this.fileContent = fileContent;
    }

    public long getPostId() {
        return postId;
    }

    public String getFileName() {
        return fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getFileContent() {
        return fileContent;
    }
}
