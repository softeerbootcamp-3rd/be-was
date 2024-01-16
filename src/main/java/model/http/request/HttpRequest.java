package model.http.request;

import model.http.Body;

public class HttpRequest {
    private final StartLine startLine;
    private final RequestHeaders headers;
    private final Body body;

    public HttpRequest(StartLine startLine, RequestHeaders headers, Body body) {
        this.startLine = startLine;
        this.headers = headers;
        this.body = body;
    }

    public StartLine getStartLine() {
        return startLine;
    }

    public RequestHeaders getHeaders() {
        return headers;
    }

    public Body getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "\nHttpRequest{" +
                "startLine=" + startLine +
                ", headers=" + headers +
                ", body=" + body +
                '}';
    }
}
