package request;

import webserver.RequestHeader;

public class HttpRequest {
    private RequestHeader requestHeader;
    private byte[] body;

    public HttpRequest(RequestHeader requestHeader, byte[] body) {
        this.requestHeader = requestHeader;
        this.body = body;
    }

    public RequestHeader getRequestHeader() {
        return requestHeader;
    }

    public byte[] getBody() {
        return body;
    }

    public static HttpRequest of(RequestHeader requestHeader, byte[] body) {
        return new HttpRequest(requestHeader, body);
    }
}
