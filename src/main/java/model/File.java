package model;

public class File {

    private long postId;
    private byte[] fileContent;

    public File(long postId, byte[] fileContent) {
        this.postId = postId;
        this.fileContent = fileContent;
    }

    public long getPostId() {
        return postId;
    }

    public byte[] getFileContent() {
        return fileContent;
    }
}
