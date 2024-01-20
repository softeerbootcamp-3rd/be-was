package dto;

import util.HttpStatus;

public class ResourceDto {
    private String path;
    private HttpStatus httpStatus = HttpStatus.OK;

    public ResourceDto(String path) {
        this.path = path;
    }

    public ResourceDto(String path, HttpStatus httpStatus) {
        this.path = path;
        this.httpStatus = httpStatus;
    }

    public static ResourceDto of(String path) {
        return new ResourceDto(path);
    }

    public static ResourceDto of(String path, int status) {
        HttpStatus httpStatus = HttpStatus.findByStatusCode(status);
        return new ResourceDto(path, httpStatus);
    }

    public String getPath() {
        return path;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}
