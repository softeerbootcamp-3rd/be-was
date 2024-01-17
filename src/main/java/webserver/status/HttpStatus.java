package webserver.status;

public enum HttpStatus {
    OK(200, "OK"),
    NOT_FOUND(404, "NOT FOUND");

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
