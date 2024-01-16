package model.http;

public class HttpResponse {
    private StatusLine statusLine;
    private ResponseHeaders headers;
    private Body body;

    public HttpResponse(StatusLine statusLine, ResponseHeaders headers, Body body) {
        this.statusLine = statusLine;
        this.headers = headers;
        this.body = body;
    }

    public StatusLine getStatusLine() {
        return statusLine;
    }

    public ResponseHeaders getHeaders() {
        return headers;
    }

    public Body getBody() {
        return body;
    }
}
