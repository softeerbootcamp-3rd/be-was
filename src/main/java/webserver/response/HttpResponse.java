package webserver.response;

import constant.HttpStatus;

import java.util.ArrayList;
import java.util.List;

public class HttpResponse {
    private HttpStatus status;
    private List<String> headers;
    private byte[] body;

    public HttpResponse() {
        this.status = null;
        this.headers = new ArrayList<>();
        this.body = null;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public List<String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public void addHeader(String key, String value) {
        this.headers.add(key + ": " + value + "\r\n");
    }

    public void setBody(byte[] body) {
        this.body = body;
    }
}
