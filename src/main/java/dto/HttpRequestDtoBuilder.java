package dto;

import model.User;

public class HttpRequestDtoBuilder {
    private final String method;

    private final String uri;

    private final String httpVersion;

    private HttpHeaders headers;

    private String body;

    private User user;

    public HttpRequestDtoBuilder(String method, String uri, String httpVersion) {
        this.method = method;
        this.uri = uri;
        this.httpVersion = httpVersion;
    }

    public HttpRequestDtoBuilder setHeaders(HttpHeaders headers) {
        this.headers = headers;
        return this;
    }

    public HttpRequestDtoBuilder setBody(String body) {
        this.body = body;
        return this;
    }

    public HttpRequestDtoBuilder setUser(User user) {
        this.user = user;
        return this;
    }

    public HttpRequestDto build() {
        return new HttpRequestDto(method, uri, httpVersion, headers, body, user);
    }
}
