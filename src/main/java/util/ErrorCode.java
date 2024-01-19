package util;

public enum ErrorCode {

    DUPLICATED_USER(400, "존재하는 사용자 입니다."),
    NOT_VALID_PATH(404, "잘못된 요청입니다.");

    private final int status;
    private String message;

    ErrorCode(int errorCode, String message) {
        this.status = errorCode;
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
