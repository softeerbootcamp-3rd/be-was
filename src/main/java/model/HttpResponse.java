package model;

import java.util.Map;

public class HttpResponse {
    private HttpHeader header;
    private byte[] body;

    private HttpResponse(HttpHeader header, byte[] body) {
        this.header = header;
        this.body = body;
    }

    public static HttpResponse emptyBodyResponse(HttpStatusCode statusCode, Map<String, String> properties) {
        return new HttpResponse(new HttpHeader(statusCode, properties), "".getBytes());
    }

    public static HttpResponse of(HttpStatusCode statusCode, byte[] body, Map<String, String> properties){
        return new HttpResponse(new HttpHeader(statusCode, properties), body);
    }

    public HttpHeader getHeader() {
        return header;
    }

    public byte[] getBody() {
        return body;
    }
}
