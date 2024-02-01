package dto;

import util.HttpStatus;

public class ResourceDto {
    private String path;
    private HttpStatus httpStatus = HttpStatus.OK;
    private boolean isloggined = true;

    public ResourceDto(String path) {
        this.path = path;
    }

    public ResourceDto(String path, HttpStatus httpStatus) {
        this.path = path;
        this.httpStatus = httpStatus;
    }

    public ResourceDto(String path, boolean isloggined) {
        this.path = path;
        this.isloggined = isloggined;
    }

    public ResourceDto(String path, HttpStatus httpStatus, boolean isloggined) {
        this.path = path;
        this.httpStatus = httpStatus;
        this.isloggined = isloggined;
    }

    public static ResourceDto of(String path) {
        return new ResourceDto(path);
    }

    public static ResourceDto of(String path, int status) {
        HttpStatus httpStatus = HttpStatus.findByStatusCode(status);
        return new ResourceDto(path, httpStatus);
    }

    public static ResourceDto of(String path, boolean isloggined) {
        return new ResourceDto(path, isloggined);
    }

    public static ResourceDto of(String path, int status, boolean islogin) {
        HttpStatus httpStatus = HttpStatus.findByStatusCode(status);
        return new ResourceDto(path, httpStatus, islogin);
    }

    public String getPath() {
        return path;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getExtension() {
        int dotIndex = path.lastIndexOf(".");
        return path.substring(dotIndex + 1);
    }

    public boolean isIsloggined() {
        return isloggined;
    }
}
