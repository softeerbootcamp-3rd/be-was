package http.response;

public enum HttpStatus {
    OK(200, "OK"),
    REDIRECT(302, "FOUND"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    NOT_FOUND(404, "Not Found"),
    SERVER_ERROR(500, "Internal Server Error");

    private final int statusCode;
    private final String message;


    HttpStatus(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public String getStatusCodeAndMessage() {
        return statusCode + " " + message;
    }

}
