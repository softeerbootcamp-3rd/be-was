package dto;

import utils.ContentType;
import utils.HttpStatus;

public class HttpResponseDto {
    private final HttpStatus status;
    private final ContentType content;
    private byte[] body;

    public HttpResponseDto(HttpStatus status, ContentType content, byte[] body) {
        this.status = status;
        this.content = content;
        this.body = body;
    }

    public HttpResponseDto(HttpStatus status, ContentType content) {
        this.status = status;
        this.content = content;
    }

    public static HttpResponseDto of(HttpStatus status, ContentType content, byte[] body) {
        return new HttpResponseDto(status, content, body);
    }

    public static HttpResponseDto of(HttpStatus status, ContentType content) {
        return new HttpResponseDto(status, content);
    }

    public HttpStatus getStatus() {
        return status;
    }

    public ContentType getContent() {
        return content;
    }

    public byte[] getBody() {
        return body;
    }
}
