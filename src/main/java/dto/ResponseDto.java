package dto;

import webserver.HttpStatus;

public class ResponseDto {

    private HttpStatus httpStatus;
    private String contentType;
    private byte[] body;

    public ResponseDto(HttpStatus httpStatus, String contentType, String str) {
        this.httpStatus = httpStatus;
        this.contentType = contentType;
        this.body = str.getBytes();
    }

    public ResponseDto(HttpStatus httpStatus, String contentType, byte[] body) {
        this.httpStatus = httpStatus;
        this.contentType = contentType;
        this.body = body;
    }

    public ResponseDto(HttpStatus httpStatus) {
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
