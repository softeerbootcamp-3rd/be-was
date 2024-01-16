package dto;

import webserver.RequestHeader;

import java.util.Map;

public class RequestDto {

    private String method;
    private String url;
    private String host;
    private String connection;
    private String accept;

    public void setMethodAndURL(String method, String url) {
        this.method = method;
        this.url = url;
    }

    public void setRequestHeaders(Map<RequestHeader, String> requestHeaders) {
        this.host = requestHeaders.get(RequestHeader.HOST);
        this.connection = requestHeaders.get(RequestHeader.CONNECTION);
        this.accept = requestHeaders.get(RequestHeader.ACCEPT);
    }

    public String getUrl() {
        return url;
    }
}
