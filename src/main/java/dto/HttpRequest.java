package dto;

public class HttpRequest {

    private String method;
    private String path;
    private String queryString;

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
}
