package model;

import util.HttpStatus;

public class CommonResponse {
    private int httpStatus;
    private byte[] body;

    private CommonResponse(int httpStatus, byte[] body){
        this.httpStatus = httpStatus;
        this.body = body;
    }

    public byte[] getBody() {
        return body;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public static CommonResponse onOk(byte[] body){
        return new CommonResponse(HttpStatus.OK.getCode(), body);
    }

    public static CommonResponse onFail(int status, String message){
        return new CommonResponse(status, message.getBytes());
    }
}
