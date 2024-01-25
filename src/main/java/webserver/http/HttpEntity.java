package webserver.http;

import java.util.List;
import java.util.Map;

public class HttpEntity<T> {

    private HttpHeader headers;
    private T body;

    public HttpEntity(HttpHeader headers, T body) {
        this.body = body;
        this.headers = headers;
    }

    public HttpEntity(Map<String, List<String>> headers, T body) {
        this.body = body;
        HttpHeader httpHeader = new HttpHeader(headers);
        this.headers = httpHeader;
    }

    public HttpHeader getHeaders() {
        return headers;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
