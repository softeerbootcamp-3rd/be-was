package DTO;

public enum HttpStatus {
    OK(200, "OK"),
    REDIRECT(302, "FOUND"),
    BAD_REQUEST(400, "Bad Request"),
    SERVER_ERROR(500, "Internal Server Error");

    private final int statusCode;
    private final String message;


    HttpStatus(int statusCode, String message) {
        this.statusCode = statusCode;
        this.message = message;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }
}