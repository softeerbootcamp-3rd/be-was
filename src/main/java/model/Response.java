package model;

import java.util.Map;

public class Response {

    private final int code;
    private String url;
    private byte[] body;

    private Map<String, String> cookie;

    public Response(int code, String url, Map<String, String> cookie) {
        this.code = code;
        this.url = url;
        this.cookie = cookie;
    }

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

    public Map<String, String> getCookie() {
        return cookie;
    }
}
