package model;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class Response {
    static final private String DEFAULT_HTTP_VER = "HTTP/1.1";

    private String httpVer;
    private String httpStatus;
    private String statusMsg;
    private Map<String, String> headerMap = new HashMap<>();
    private byte[] body;

    public void putToHeaderMap(String key, String value) {
        headerMap.put(key, value);
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void set200Ok() {
        httpVer = DEFAULT_HTTP_VER;
        httpStatus = "200";
        statusMsg = "ok";
    }

    public void set302Redirect() {
        httpVer = DEFAULT_HTTP_VER;
        httpStatus = "302";
        statusMsg = "redirect";
    }

    public String extractLine() {
        StringBuilder sb = new StringBuilder();
        sb.append(httpVer).append(" ").append(httpStatus).append(" ").append(statusMsg).append("\r\n");

        return sb.toString();
    }

    public String extractHeader() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : headerMap.entrySet()) {
            sb.append(entry.getKey()).append(": ").append(entry.getValue()).append("\r\n");
        }
        sb.append("\r\n");

        return sb.toString();
    }

    public byte[] getBody() {
        return body;
    }
}
