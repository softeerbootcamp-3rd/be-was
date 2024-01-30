package file;

public class MultipartFile {

    private String fieldName;
    private String contentType;
    private byte[] content;

    public MultipartFile(String fieldName, String contentType, byte[] content) {
        this.fieldName = fieldName;
        this.contentType = contentType;
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
