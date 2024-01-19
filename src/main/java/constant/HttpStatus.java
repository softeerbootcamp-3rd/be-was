package constant;

public enum HttpStatus {
    OK ("200", "OK"),
    CREATED ("201", "Created"),
    REDIRECT ("302", "Found"),
    BAD_REQUEST ("400", "Bad Request"),
    NOT_FOUND ("401", "Not Found"),
    INTERNAL_SERVER_ERROR ("500", "Internal Server Error");

    private final String code;
    private final String message;

    HttpStatus(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getStatus() {
        return code + " " + message;
    }
}
