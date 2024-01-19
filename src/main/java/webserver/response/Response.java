package webserver.response;

import webserver.status.HttpStatus;
import webserver.type.ContentType;


public class Response {
    private final HttpStatus httpStatus;
    private final ContentType contentType;
    private final byte[] body;

    private Response(HttpStatus httpStatus, ContentType contentType, byte[] body){
        this.httpStatus = httpStatus;
        this.contentType = contentType;
        this.body = body;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public ContentType getContentType() {
        return contentType;
    }

    public byte[] getBody() {
        return body;
    }

    public static Response onSuccess(ContentType contentType, byte[] body){
        return new Response(HttpStatus.OK, contentType, body);
    }

    public static Response onFailure(HttpStatus httpStatus, ContentType contentType, byte[] body){
        return new Response(httpStatus, contentType, body);
    }
}
