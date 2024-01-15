package webserver;

public class HttpRequestParam {
    private final String method;

    private final String uri;
    private String path;

    private String contentType;

    public HttpRequestParam(String method, String uri) {
        this.method = method;
        this.uri = uri;
        parseUri(uri);
    }

    public String getMethod() {
        return this.method;
    }

    public String getUri() {
        return this.uri;
    }

    public String getPath() {
        return this.path;
    }

    public String getContentType() {
        return this.contentType;
    }

    void parseUri(String uri) {
        String[] parsedUri = uri.split("\\.");
        this.contentType = parsedUri[parsedUri.length - 1];
        if (this.contentType.equals("html")) {
            this.path = "./src/main/resources/templates" + this.uri;
        } else {
            this.path = "./src/main/resources/static" + this.uri;
        }
    }
}
