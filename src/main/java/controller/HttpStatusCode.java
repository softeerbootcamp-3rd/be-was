package controller;

public enum HttpStatusCode {
    OK("200 OK"),
    FOUND("302 Found"),
    BAD_REQUEST("400 Bad Request"),
    NOT_FOUND("404 Not Found"),
    INTERNAL_SERVER_ERROR("500 Internal Server Error");

    private final String statusCode;
    HttpStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusCode() {
        return statusCode;
    }
}
