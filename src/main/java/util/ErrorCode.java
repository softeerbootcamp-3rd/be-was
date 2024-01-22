package util;

public enum ErrorCode {

    DUPLICATED_USER(HttpStatus.BAD_REQUEST, "존재하는 사용자 입니다."),
    NOT_VALID_PATH(HttpStatus.NOT_FOUND, "잘못된 요청입니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
