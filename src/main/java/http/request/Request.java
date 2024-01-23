package http.request;

public class Request {
    private static final String ROOT_PATH = "src/main/resources/";

    private final StartLine startLine;
    private final Headers header;
    private final Body body;

    public Request(StartLine startLine, Headers header, Body body) {
        this.startLine = startLine;
        this.header = header;
        this.body = body;
    }

    public String getUrl() {
        return startLine.getUrl();
    }

    public String getVersion() {
        return startLine.getHttpVersion();
    }

    public String getFilePath() {
        String url = startLine.getUrl();
        if (url.endsWith(".html")) {
            return ROOT_PATH + "templates" + url;
        }
        return ROOT_PATH + "static" + url;
    }

    public HttpMethod getMethod() {
        return startLine.getMethod();
    }

    @Override
    public String toString() {
        return "Request [method=" + startLine.getMethod() + ", url=" + startLine.getUrl() + ", body=" + body.getBody() + "]";
    }

    public String getBody() {
        return body.getBody();
    }
}