package http.builder;

import http.HttpRequest;

import java.util.Map;

public class HttpRequestBuilder {

    private String method;
    private String uri;
    private String queryString;
    private Map<String, String> headerFields;
	private String body;

    public HttpRequestBuilder method(String method) {
        this.method = method;
        return this;
    }

    public HttpRequestBuilder uri(String uri) {
        this.uri = uri;
        return this;
    }

    public HttpRequestBuilder queryString(String queryString) {
        this.queryString = queryString;
        return this;
    }

    public HttpRequestBuilder headerFields(Map<String,String> headerFields) {
        this.headerFields = headerFields;
        return this;
    }

	public HttpRequestBuilder body(String body) {
		this.body = body;
		return this;
	}
    public HttpRequest build() {
        return new HttpRequest(method, uri, queryString, headerFields, body);
    }
}
