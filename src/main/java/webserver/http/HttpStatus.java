package webserver.http;

public enum HttpStatus {

    OK(200, "Ok"),
    FOUND(302, "Found"),
    BAD_REQUEST(400, "Bad_Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    NOT_FOUND(404, "Not_Found"),
    INTERNAL_SERVER_ERROR(500, "Internal_Server_Error");


    private Integer statusCode;
    private String statusMessage;

    HttpStatus(int statusCode, String statusMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }
}
