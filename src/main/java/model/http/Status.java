package model.http;

public enum Status {
    OK(200, "OK"), MOVED_PERMANENTLY(301, "Moved Permanently"), BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"), FORBIDDEN(403, "Forbidden"), NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error"),REDIRECT(302, "Temporarily Moved");
    private final Integer statusCode;
    private final String statusText;

    Status(Integer statusCode, String statusText) {
        this.statusCode = statusCode;
        this.statusText = statusText;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getStatusText() {
        return statusText;
    }
}