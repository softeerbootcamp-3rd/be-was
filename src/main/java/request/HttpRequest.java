package request;

import webserver.RequestHeader;

public class HttpRequest {
    private RequestHeader requestHeader;
    private String body;

    public HttpRequest(RequestHeader requestHeader, String body) {
        this.requestHeader = requestHeader;
        this.body = body;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public String getBody() {
        return body;
    }

    public static HttpRequest of(RequestHeader requestHeader, String body) {
        return new HttpRequest(requestHeader, body);
    }
}
