package model;

import util.HttpStatus;

public class CommonResponse {
    private HttpStatus httpStatus;
    private byte[] body;
    private String path;

    private String extension = "html";

    private CommonResponse(HttpStatus httpStatus, byte[] body, String extension, String path){
        this.httpStatus = httpStatus;
        this.body = body;
        this.extension = extension;
        this.path = path;
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

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    public String getExtension() {
        return extension;
    }

    public static CommonResponse onOk(HttpStatus httpStatus, byte[] body, String extension, String path){
        return new CommonResponse(httpStatus, body, extension, path);
    }

    public static CommonResponse onFail(HttpStatus httpStatus, String message){
        return new CommonResponse(httpStatus, message.getBytes());
    }
}
