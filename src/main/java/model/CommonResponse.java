package model;

import util.HttpStatus;

public class CommonResponse {
    private HttpStatus httpStatus;
    private byte[] body;
    private String extension = "html";

    private CommonResponse(HttpStatus httpStatus, byte[] body, String extension){
        this.httpStatus = httpStatus;
        this.body = body;
        this.extension = extension;
    }

    private CommonResponse(HttpStatus httpStatus, byte[] body){
        this.httpStatus = httpStatus;
        this.body = body;
    }

    public byte[] getBody() {
        return body;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getExtension() {
        return extension;
    }

    public static CommonResponse onOk(HttpStatus httpStatus, byte[] body, String extension){
        return new CommonResponse(httpStatus, body, extension);
    }

    public static CommonResponse onFail(HttpStatus httpStatus, String message){
        return new CommonResponse(httpStatus, message.getBytes());
    }
}
