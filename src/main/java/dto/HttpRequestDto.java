package dto;

import model.User;

public class HttpRequestDto {
    private final String method;

    private final String uri;

    private final String httpVersion;

    private final HttpHeaders headers;

    private final String body;

    private final User user;

    public HttpRequestDto(String method, String uri, String httpVersion, HttpHeaders headers, String body, User user) {
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
        this.headers = headers;
        this.body = body;
        this.user = user;
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

    public HttpHeaders getHeaders() {
        return this.headers;
    }

    public String getBody() {
        return this.body;
    }

    public User getUser() {
        return this.user;
    }

    // HTTP Request 로그 출력 용 코드
    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Requeset Line: ").append(this.method + " " + this.uri + " " + this.httpVersion);
        return stringBuilder.toString();
    }
}
