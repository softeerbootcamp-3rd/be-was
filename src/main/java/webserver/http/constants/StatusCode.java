package webserver.http.constants;

public enum StatusCode {
    OK(200),
    FOUND(302),
    BAD_REQUEST(400),
    UNAUTHORIZED(401),
    NOT_FOUND(404),
    METHOD_NOT_ALLOWED(405);

    private final int code;

    StatusCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    public static StatusCode fromString(String codeString) {
        int code = Integer.parseInt(codeString);
        for (StatusCode statusCode : StatusCode.values()) {
            if (statusCode.getCode() == code) {
                return statusCode;
            }
        }
        throw new IllegalArgumentException("No constant with code " + codeString + " found");
    }
}
