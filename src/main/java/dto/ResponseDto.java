package dto;

public class ResponseDto {

    private final String url;
    private final int code;
    private final byte[] body;

    public ResponseDto() {
        this.url = "/";
        this.code = 200;
        this.body = "NULL".getBytes();
    }

    public ResponseDto(String path, int code, byte[] body) {
        this.url = path;
        this.code = code;
        this.body = body;
    }

    public String getUrl() {
        return url;
    }

    public int getCode() {
        return code;
    }

    public byte[] getBody() {
        return body;
    }
}
