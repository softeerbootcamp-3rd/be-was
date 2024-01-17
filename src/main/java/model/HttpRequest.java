package model;

import java.util.Map;

public class HttpRequest {
    private String url;
    private String method;
    private Map<String, String> queryString;

    public HttpRequest(String method, String url, Map<String, String> queryString) {
        this.method = method;
        this.url = url;
        this.queryString = queryString;
    }

    public String getUrl() {
        return url;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getQueryString() {
        return queryString;
    }
}
