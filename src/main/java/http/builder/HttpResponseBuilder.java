package http.builder;

import http.HttpResponse;
import http.status.HttpStatusCode;

import java.util.Map;
import java.util.Optional;

public class HttpResponseBuilder {
    private HttpStatusCode statusCode;
    private Map<String, String> headerFields;
    private byte[] body;

    public HttpResponseBuilder status(HttpStatusCode statusCode) {
        this.statusCode = statusCode;
        return this;
    }
    public HttpResponseBuilder headerFields(Map<String, String> headerFields) {
        this.headerFields = headerFields;
        return this;
    }

    public HttpResponseBuilder body(byte[] body) {
		this.body = body;
        return this;
    }

    public HttpResponse build() {
        return new HttpResponse(statusCode, headerFields, body);
    }
}
