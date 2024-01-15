package util;

public enum HttpStatus {
    OK("200", "OK"),

    BAD_REQUEST("400", "Bad Request"),
    NOT_FOUND("404", "Not Found"),

    INTERNAL_SERVER_ERROR("500", "Internal Server Error");

    private final String code;
    private final String message;

    HttpStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public String getFullMessage() {
        return code + " " + message;
    }

    public static HttpStatus getByCode(String code) {
        for (HttpStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null; // 해당 코드를 가진 상태가 없을 경우
    }
}