package constant;

import webserver.HttpStatus;

public enum ErrorCode {
    PAGE_NOT_FOUND (HttpStatus.NOT_FOUND, "요청한 페이지를 찾을 수 없습니다."),
    USER_ID_DUPLICATED (HttpStatus.BAD_REQUEST, "중복되는 유저 id 입니다."),
    USER_EMAIL_DUPLICATED (HttpStatus.BAD_REQUEST, "중복되는 유저 이메일 입니다.");

    private final HttpStatus httpStatus;
    private final String errorMessage;

    ErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
