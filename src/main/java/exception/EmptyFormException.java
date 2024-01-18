package exception;

public class EmptyFormException extends RuntimeException{

    public EmptyFormException() {
        super("회원가입 폼을 모두 기입하지 않았습니다. 폼을 모두 작성해주세요");
    }

    public EmptyFormException(String message) {
        super(message);
    }
}
