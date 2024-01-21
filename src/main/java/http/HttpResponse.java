package http;

import http.header.ResponseHeader;
import http.status.HttpStatus;

import java.util.Map;

public class HttpResponse {
    private ResponseHeader header;
    private byte[] body;

    private HttpResponse(ResponseHeader header, byte[] body) {
        this.header = header;
        this.body = body;
    }

    public static HttpResponse of(HttpStatus status, byte[] body, Map<String, String> properties){
        return new HttpResponse(ResponseHeader.of(status, properties), body);
    }

    public static HttpResponse of() {
        return new HttpResponse(ResponseHeader.of(), "".getBytes());
    }

    public ResponseHeader getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }

    public void setHeader(ResponseHeader header) {
        this.header = header;
    }

    public void setBody(byte[] body) {
        this.body = body;
    }

    public void setEmptyBody() {
        this.body = "".getBytes();
    }
}
