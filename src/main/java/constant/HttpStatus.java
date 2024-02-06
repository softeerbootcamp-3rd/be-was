package constant;

public enum HttpStatus {
    OK("200", "OK"),
    CREATED("201", "Created"),

    FOUND("302", "Found"),

    BAD_REQUEST("400", "Bad Request"),
    FORBIDDEN("403", "Forbidden"),
    NOT_FOUND("404", "Not Found"),
    CONFLICT("409", "Conflict"),

    INTERNAL_SERVER_ERROR("500", "Internal Server Error");

    private final String code;
    private final String message;

    HttpStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return this.code;
    }

    public String getMessage() {
        return this.message;
    }

    public String getFullMessage() {
        return this.code + " " + this.message;
    }

    public static HttpStatus getByCode(String code) {
        for (HttpStatus status : values()) {
            if (status.code.equals(code)) {
                return status;
            }
        }
        return null;
    }
}
