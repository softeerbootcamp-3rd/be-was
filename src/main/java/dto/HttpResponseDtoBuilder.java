package dto;

import java.util.HashMap;
import java.util.Map;

public class HttpResponseDtoBuilder {
    private String status;
    private String message;
    private final Map<String, String> headers;
    private byte[] body;

    public HttpResponseDtoBuilder() {
        this.headers = new HashMap<>();
    }

    public HttpResponseDtoBuilder setStatus(String status) {
        this.status = status;
        return this;
    }

    public HttpResponseDtoBuilder setMessage(String message) {
        this.message = message;
        return this;
    }

    public HttpResponseDtoBuilder setHeaders(String key, String value) {
        headers.put(key, value);
        return this;
    }

    public HttpResponseDtoBuilder setBody(byte[] body) {
        this.body = body;
        return this;
    }

    public HttpResponseDto build() {
        return new HttpResponseDto(status, message, headers, body);
    }

    public HttpResponseDtoBuilder response302Header() {
        setStatus("302").setMessage("Found");
        return this;
    }

    public HttpResponseDtoBuilder setCookie(String key, String value, String[] attributes) {
        StringBuilder cookies = new StringBuilder().append(key).append("=").append(value);
        for (String attribute : attributes) {
            cookies.append("; ").append(attribute);
        }
        setHeaders("Set-Cookie", cookies.toString());
        return this;
    }
}
