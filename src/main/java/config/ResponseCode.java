package config;

public enum ResponseCode {
    //숫자는 임시
    OK(200),
    REDIRECT(302),
    NOT_FOUND(404),
    SERVER_ERROR(500);

    public final int code;
    private ResponseCode(int code){
        this.code = code;
    }
}
