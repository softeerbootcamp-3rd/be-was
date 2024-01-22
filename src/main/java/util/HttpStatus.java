package util;

public enum HttpStatus {

    OK(200, "OK"),
    BAD_REQUEST(400, "Bad Request"),
    NOT_FOUND(404, "Not Found"),

    REDIRECT(302, "Found");


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

    public static HttpStatus findByStatusCode(int statusCode) {
        for (HttpStatus status : values()) {
            if (status.getCode() == statusCode) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + statusCode);
    }

}
