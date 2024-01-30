package exception;

import http.HttpStatus;

public enum ErrorCode {

    USER_ALREADY_EXISTS(HttpStatus.CONFLICT, "사용자가 이미 존재합니다."),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자가 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시글이 없습니다."),
    ;


    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public HttpStatus getHttpStatus() { return httpStatus; }

}
