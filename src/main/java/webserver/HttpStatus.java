package webserver;

public enum HttpStatus {
    OK("200", "OK"),
    CREATED("201", "Created"),
    NO_CONTENT("204", "No Content"),
    FOUND("302", "Found"),
    BAD_REQUEST("400", "Bad Request"),
    FORBIDDEN("403", "Forbidden"),
    NOT_FOUND("404", "Not Found"),
    INTERNAL_SERVER_ERROR("500", "Internal Server Error");

    private String code;
    private String status;

    HttpStatus(String code, String status) {
        this.code = code;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }

}
