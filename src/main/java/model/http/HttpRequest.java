package model.http;

public class HttpRequest {
    private StartLine startLine;
    private RequestHeaders headers;
    private Body body;

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
