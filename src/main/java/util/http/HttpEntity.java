package util.http;

import util.MultiValueMap;

public class HttpEntity<T> {
    private final HttpHeaders headers;
    private final T body;

    protected HttpEntity() {
        this(null, null);
    }

    public HttpEntity(T body) {
        this(body, null);
    }

    public HttpEntity(MultiValueMap<String, String> headers) {
        this(null, headers);
    }

    public HttpEntity(T body, MultiValueMap<String, String> headers) {
        this.body = body;
        this.headers = headers != null ? new HttpHeaders(headers) : new HttpHeaders();
    }

    public HttpHeaders getHeaders() {
        return this.headers;
    }

    public T getBody() {
        return this.body;
    }

    public boolean hasBody() {
        return (this.body != null);
    }
}
