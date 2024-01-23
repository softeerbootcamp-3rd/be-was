package http.request;

public class StartLine {
    private HttpMethod method;
    private String url;
    private String httpVersion;

    public StartLine(String method, String url, String httpVersion) {
        this.method = HttpMethod.valueOf(method);;
        this.url = url;
        this.httpVersion = httpVersion;
    }

    public HttpMethod getMethod() {
        return method;
    }

    public String getUrl() {
        return url;
    }

    public String getHttpVersion() {
        return httpVersion;
    }
}
