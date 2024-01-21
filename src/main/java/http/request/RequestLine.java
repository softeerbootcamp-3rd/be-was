package http.request;

import java.util.Map;

public class RequestLine {
    private String method;
    private String uri;
    private String version;

    public RequestLine(Map<String, String> params) {
        this.method = params.get("method");
        this.uri = params.get("uri");
        this.version = params.get("version");
    }

    public String getMethod() {
        return method;
    }

    public String getUri() {
        return uri;
    }

    public String getVersion() {
        return version;
    }
}
