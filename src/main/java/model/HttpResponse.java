package model;

import com.google.common.collect.Maps;
import constant.HttpStatus;
import java.util.Map;

public class HttpResponse {
    private static final String DEFAULT_HTTP_VERSION = "HTTP1.1";
    private String httpVer;
    private HttpStatus httpStatus;
    private Map<String, String> headerMap = Maps.newHashMap();
    private byte[] body;

    public void addHeader(String key, String value) {
        headerMap.put(key, value);
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void set200Ok() {
        httpVer = DEFAULT_HTTP_VERSION;
        httpStatus = HttpStatus.OK;
    }

    public void set302Redirect() {
        httpVer = DEFAULT_HTTP_VERSION;
        httpStatus = HttpStatus.FOUND;
    }

    public String extractLine() {
        StringBuilder sb = new StringBuilder();
        sb.append(httpVer).append(" ").append(httpStatus.getCode()).append(" ").append(httpStatus.getMessage()).append("\r\n");

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
