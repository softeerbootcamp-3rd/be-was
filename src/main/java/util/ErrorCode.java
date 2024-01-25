package util;

public enum ErrorCode {

    DUPLICATED_USER(HttpStatus.BAD_REQUEST, "존재하는 사용자 입니다."),
    NOT_VALID_PATH(HttpStatus.NOT_FOUND, "잘못된 요청입니다."),
    NOT_EXIST_USER(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자 입니다."),
    NOT_VALID_PASSWORD(HttpStatus.BAD_REQUEST, "비밀번호가 일치하지 않습니다.");

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
