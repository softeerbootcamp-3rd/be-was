package config;

public enum ResponseCode {
    OK(200),
    NOT_FOUND(404),
    REDIRECT(300);
    public final int code;
    private ResponseCode(int code){
        this.code = code;
    }
}
