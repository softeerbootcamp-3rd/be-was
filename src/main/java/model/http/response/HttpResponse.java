package model.http.response;

import model.http.Body;

public class HttpResponse {
    private final StatusLine statusLine;
    private final ResponseHeaders headers;
    private final Body body;

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