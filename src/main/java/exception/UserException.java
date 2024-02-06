package exception;

public class UserException extends RuntimeException {
    public static final String NULL_ID = "아이디를 입력해 주세요.";
    public static final String NULL_PASSWORD = "비밀번호를 입력해 주세요.";
    public static final String NULL_NAME = "이름을 입력해 주세요.";
    public static final String NULL_EMAIL = "이메일을 입력해 주세요.";
    public static final String DUPLICATE_ID = "이미 존재하는 아이디입니다. 다른 아이디를 입력해 주세요.";
    public static final String DUPLICATE_EMAIL = "이미 존재하는 이메일입니다. 다른 이메일을 입력해 주세요.";


    public UserException(String message) {
        super(message);
    }
}
