package model;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Response {

    private int code;
    private final Map<String, String> header;
    private byte[] body;

    public Response() {
        this.header = new HashMap<>();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public byte[] getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body.getBytes();
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void addHeader(String key, String value) {
        header.put(key, value);
    }

    public Map<String, String> getHeaderKey() {
        return Collections.unmodifiableMap(header);
    }
}
