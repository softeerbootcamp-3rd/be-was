package webserver;

public enum HttpStatus {

    OK(200, "Ok"),
    CREATED(201, "Created"),
    NO_CONTENT(204, "No_Content"),
    FOUND(302, "Redirect"),
    BAD_REQUEST(400, "Bad_Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
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
