package exception;

public class DuplicateUserException extends RuntimeException {
    public static final String duplicateId = "이미 존재하는 아이디입니다. 다른 아이디로 가입해주세요.";
    public static final String duplicateEmail = "이미 존재하는 이메일입니다. 다른 이메일로 가입해주세요.";


    public DuplicateUserException(String message) {
        super(message);
    }
}
