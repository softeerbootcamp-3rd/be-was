package webserver.status;

public enum HttpStatus {
    OK(200, "OK"),

    BAD_REQUEST(400, "BAD REQUEST"),
    NOT_FOUND(404, "NOT FOUND"),
    CONFLICT(409, "CONFLICT"),

    INTERNAL_SERVER_ERROR(500, "INTERNAL SERVER ERROR");

    private final Integer code;
    private final String name;

    HttpStatus(Integer code, String name){
        this.code = code;
        this.name = name;
    }

    public Integer getCode() {
        return code;
    }

    public String getName() {
        return name;
    }
}
