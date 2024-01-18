package common.response;

public enum Status {

    SUCCESS("200", "OK"),
    REDIRECT("302", "Found"),
    BAD_REQUEST("400", "Bad Request"),
    CONFLICT("409", "Conflict");

    private final String code;
    private final String msg;

    Status(String statusCode, String msg) {
        this.code = statusCode;
        this.msg = msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
