package dto;

import java.util.Map;

public class HttpRequest {

    private String method;
    private String path;
    private String queryString;
    private Map<String, String> headers;

    public HttpRequest(String method, String path, String queryString) {
        this.method = method;
        this.path = path;
        this.queryString = queryString;
    }

    public HttpRequest(String method, String path) {
        this.method = method;
        this.path = path;
        this.queryString = null;
    }

    public String getMethod() {
        return method;
    }

    public String getPath() {
        return path;
    }

    public String getQueryString() {
        return queryString;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }
}
