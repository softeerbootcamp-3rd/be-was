package common.exception;

public class LoginFailException extends RuntimeException {

    public LoginFailException() {
        super("비밀번호가 일치하지 않아 로그인에 실패했습니다.");
    }

    public LoginFailException(String message) {
        super(message);
    }
}
