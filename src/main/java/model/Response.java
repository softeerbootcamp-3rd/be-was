package model;

public class Response {

    private final String url;
    private final int code;
    private final byte[] body;

    // todo 생성자 오버로딩 만들기

    public Response(String path, int code, byte[] body) {
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
