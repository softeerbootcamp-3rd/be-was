package webserver.http.response.enums;

public enum HttpStatus {
    // 성공 상태 코드
    OK(200, "OK"),
    CREATED(201, "Created"),

    // 리다이렉션 상태 코드
    MOVED_PERMANENTLY(301, "Moved Permanently"),
    FOUND(302, "Found"),
    SEE_OTHER(303, "See Other"),

    // 클라이언트 에러 상태 코드
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),

    // 서버 에러 상태 코드
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int code;
    private final String text;

    HttpStatus(int code, String text) {
        this.code = code;
        this.text = text;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
