package webserver;

public enum Status {

    OK("200", "OK"),
    REDIRECT("302", "Found"),
    BAD_REQUEST("400", "Bad Request"),
    NOT_FOUND("404", "Not Found"),
    CONFLICT("409", "Conflict");

    private final String code;
    private final String msg;

    Status(String statusCode, String msg) {
        this.code = statusCode;
        this.msg = msg;
    }

    @Override
    public String toString() {
        return code + " " + msg;
    }
}
