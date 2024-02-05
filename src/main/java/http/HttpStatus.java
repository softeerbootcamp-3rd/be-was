package http;

public enum HttpStatus {
    OK(200, "OK"),
    CREATED(201, "Created"),
    FOUND(302, "Found (HTTP 1.0)"),
    MOVED_PERMANENTLY(301, "Moved Permanently"),
    NOT_MODIFIED(304, "Not Modified"),
    BAD_REQUEST(400,"Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),
    SERVICE_UNAVAILABLE(503, "Service Unavailable"),
    ;

    private final int code;
    private final String description;

    HttpStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }

    public int getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}
