package dto;

import java.util.Map;

public class HttpRequestDto {
    private final String method;

    private final String uri;

    private final String httpVersion;

    private final Map<String, String> headers;

    private final String body;

    public HttpRequestDto(String method, String uri, String httpVersion, Map<String, String> headers, String body) {
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.body = body;
    }

    public String getMethod() {
        return this.method;
    }

    public String getUri() {
        return this.uri;
    }

    public String getHttpVersion() {
        return this.httpVersion;
    }

    public Map<String, String> getHeaders() {
        return this.headers;
    }

    public String getBody() {
        return this.body;
    }

    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Requeset Line: ").append(this.method + " " + this.uri + " " + this.httpVersion);
        return stringBuilder.toString();
    }
}
