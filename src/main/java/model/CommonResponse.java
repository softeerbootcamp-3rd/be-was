package model;

import util.HttpStatus;

public class CommonResponse {
    private HttpStatus httpStatus;
    private byte[] body;

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

    public static CommonResponse onOk(HttpStatus httpStatus, byte[] body){
        return new CommonResponse(httpStatus, body);
    }

    public static CommonResponse onFail(HttpStatus httpStatus, String message){
        return new CommonResponse(httpStatus, message.getBytes());
    }
}
