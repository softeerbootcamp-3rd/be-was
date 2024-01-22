package model;

public class Response {

    private final int code;
    private String url;
    private byte[] body;

    public Response(int code, String url) {
        this.code = code;
        this.url = url;
    }

    public Response(int code, byte[] body) {
        this.code = code;
        this.body = body;
    }

    public Response(int code, String url, byte[] body) {
        this.url = url;
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
