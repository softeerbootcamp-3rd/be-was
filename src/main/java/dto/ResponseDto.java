package dto;

import util.HttpStatus;

public class ResponseDto<T> {

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

    public ResponseDto(HttpStatus httpStatus, String contentType) {
        this.httpStatus = httpStatus;
        this.contentType = contentType;
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
