package dto;

import webserver.HttpStatus;

public class ResponseBuilder {

    private HttpStatus httpStatus;
    private String contentType;
    private byte[] body;

    public ResponseBuilder(HttpStatus httpStatus, String contentType, String str) {
        this.httpStatus = httpStatus;
        this.contentType = contentType;
        this.body = str.getBytes();
    }

    public ResponseBuilder(HttpStatus httpStatus, String contentType, byte[] body) {
        this.httpStatus = httpStatus;
        this.contentType = contentType;
        this.body = body;
    }

    public ResponseBuilder(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.contentType = null;
        this.body = null;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getContentType() {
        return contentType;
    }

    public byte[] getBody() {
        return body;
    }



}
