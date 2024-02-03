package file;

public class MultipartFile {

    private String fieldName;
    private String contentType;
    private String fileName;
    private byte[] content;

    public MultipartFile(String fieldName, String fileName, String contentType, byte[] content) {
        this.fieldName = fieldName;
        this.fileName = fileName;
        this.contentType = contentType;
        this.content = content;
    }

    public String getFileName() {
        return fileName;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    public String getFieldName() {
        return fieldName;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getContent() {
        return content;
    }

}
