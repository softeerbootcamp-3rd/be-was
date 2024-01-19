package dto;

import java.util.Map;
import java.util.Optional;

public class HttpResponseDto {
    private final String status;
    private final String message;
    private final Map<String, String> headers;
    private final byte[] body;

    public HttpResponseDto(String status, String message, Map<String, String> headers, byte[] body) {
        this.status = status;
        this.message = message;
        this.headers = headers;
        this.body = Optional.ofNullable(body).orElse(new byte[0]);
    }

    public String getHttpVersion() {
        return "HTTP/1.1";
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }
}
