package webserver.status;

public enum ErrorCode {
    RESOURCE_NOT_FOUND_ERROR(HttpStatus.NOT_FOUND, "요청하신 데이터를 찾을 수 없습니다."),
    ILLEGAL_ARGUMENT_ERROR(HttpStatus.BAD_REQUEST, "잘못된 입력입니다."),
    USER_ID_ALREADY_EXISTS_ERROR(HttpStatus.CONFLICT, "현재 사용중인 아이디입니다."),
    LOGIN_FAILED_ERROR(HttpStatus.BAD_REQUEST, "아이디 또는 비밀번호가 틀립니다. 다시 로그인 해주세요."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러가 발생했습니다.");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message){
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
