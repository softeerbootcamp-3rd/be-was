package webserver.http;

public enum StatusCode {
    OK(200),
    BAD_REQUEST(400),
    UNAUTHORIZED(401);

    private final int code;

    StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
