package dto;

import webserver.RequestHeader;

import java.util.Map;

public class RequestHeaderDto {

    private String host;
    private String connection;
    private String userAgent;
    private String accept;

    public RequestHeaderDto(Map<RequestHeader, String> requestHeaders) {
        this.host = requestHeaders.get(RequestHeader.HOST);
        this.connection = requestHeaders.get(RequestHeader.CONNECTION);
        this.userAgent = requestHeaders.get(RequestHeader.USER_AGENT);
        this.accept = requestHeaders.get(RequestHeader.ACCEPT);
    }
}
