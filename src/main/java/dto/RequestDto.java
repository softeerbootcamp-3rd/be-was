package dto;

import webserver.RequestHeader;

import java.util.Map;

public class RequestDto {

    private String method;
    private String path;
    private String host;
    private String connection;
    private String userAgent;
    private String accept;

    public void setMethodAndPath(String method, String path) {
        this.method = method;
        this.path = path;
    }

    public void setRequestHeaders(Map<RequestHeader, String> requestHeaders) {
        this.host = requestHeaders.get(RequestHeader.HOST);
        this.connection = requestHeaders.get(RequestHeader.CONNECTION);
        this.userAgent = requestHeaders.get(RequestHeader.USER_AGENT);
        this.accept = requestHeaders.get(RequestHeader.ACCEPT);
    }

    public String getPath() {
        return path;
    }
}
