package common.exception;

public class DuplicateUserIdException extends RuntimeException {

    public DuplicateUserIdException() {
        super("중복된 userId 입니다. 다른 userId로 회원가입을 진행해주세요.");
    }

    public DuplicateUserIdException(String message) {
        super(message);
    }
}
