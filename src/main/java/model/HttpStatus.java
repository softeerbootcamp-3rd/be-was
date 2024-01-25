package model;

public enum HttpStatus {
    OK("200", "OK"),
    FOUND("302", "Found"),
    BAD_REQUEST("400", "Bad Request"),
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
