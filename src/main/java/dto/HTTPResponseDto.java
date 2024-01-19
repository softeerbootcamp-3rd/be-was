package dto;

public class HTTPResponseDto<T> {
    private int statusCode;
    private byte[] contents;

    public HTTPResponseDto() {
        this.statusCode = 500;
        this.contents = null;
    }
    public HTTPResponseDto(int statusCode, byte[] contents) {
        this.statusCode = statusCode;
        this.contents = contents;
    }

    public int getStatusCode() {
        return this.statusCode;
    }
    public byte[] getContents() {
        return this.contents;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
    public void setContents(byte[] contents) {
        this.contents = contents;
    }
}
