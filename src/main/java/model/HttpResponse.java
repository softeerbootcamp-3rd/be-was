package model;

import com.google.common.collect.Maps;
import constant.HeaderType;
import constant.HttpStatus;
import java.util.Map;

public class HttpResponse {
    private static final String DEFAULT_HTTP_VERSION = "HTTP1.1";
    private String httpVer;
    private HttpStatus httpStatus;
    private Map<HeaderType, String> headerMap = Maps.newHashMap();
    private byte[] body;

    public void addHeader(HeaderType type, String value) {
        headerMap.put(type, value);
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        httpVer = DEFAULT_HTTP_VERSION;
        this.httpStatus = httpStatus;
    }

    public void set200Ok() {
        httpVer = DEFAULT_HTTP_VERSION;
        httpStatus = HttpStatus.OK;
    }

    public void set302Redirect() {
        httpVer = DEFAULT_HTTP_VERSION;
        httpStatus = HttpStatus.FOUND;
    }

    public String extractStartLine() {
        StringBuilder sb = new StringBuilder();
        sb.append(httpVer).append(" ").append(httpStatus.getCode()).append(" ").append(httpStatus.getMessage()).append("\r\n");

        return sb.toString();
    }

    public String extractHeader() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<HeaderType, String> entry : headerMap.entrySet()) {
            sb.append(entry.getKey().getType()).append(": ").append(entry.getValue()).append("\r\n");
        }
        sb.append("\r\n");
        return sb.toString();
    }

    public byte[] getBody() {
        return body;
    }

    public static HttpResponse newEmptyInstance() {
        return new HttpResponse();
    }
}
