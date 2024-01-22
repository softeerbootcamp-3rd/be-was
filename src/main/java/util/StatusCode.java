package util;

public enum StatusCode {
    OK(200),
    FOUND(302),
    NOT_FOUND(404);

    private final Integer status;

    StatusCode(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}
