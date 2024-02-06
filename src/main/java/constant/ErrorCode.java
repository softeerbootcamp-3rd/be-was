package constant;

public enum ErrorCode {
    PAGE_NOT_FOUND (HttpStatus.NOT_FOUND, "요청한 페이지를 찾을 수 없습니다."),
    NO_ENOUGH_INPUT (HttpStatus.BAD_REQUEST, "입력하지 않은 정보가 있습니다."),
    USER_ID_DUPLICATED (HttpStatus.BAD_REQUEST, "중복되는 유저 id 입니다."),
    USER_EMAIL_DUPLICATED (HttpStatus.BAD_REQUEST, "중복되는 유저 이메일 입니다."),
    USER_NOT_FOUND (HttpStatus.BAD_REQUEST, "유저가 존재하지 않습니다."),
    USER_WRONG_PASSWORD (HttpStatus.BAD_REQUEST, "비밀번호가 틀렸습니다."),
    SERVER_ERROR (HttpStatus.INTERNAL_SERVER_ERROR, "서버에 문제 발생, 다음에 시도해주세요.");

    public final HttpStatus httpStatus;
    public final String errorMessage;

    ErrorCode(HttpStatus httpStatus, String errorMessage) {
        this.errorMessage = errorMessage;
        this.httpStatus = httpStatus;
    }
}
