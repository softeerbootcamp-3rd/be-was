package util;

public enum HttpStatus {

    OK(200, "OK"),
    BAD_REQUEST(404, "BAD_REQUEST"),
    REDIRECT(302, "REDIRECT");


    private final int code;
    private final String message;

    HttpStatus(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
