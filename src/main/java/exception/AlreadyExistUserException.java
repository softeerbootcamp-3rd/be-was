package exception;

public class AlreadyExistUserException extends RuntimeException {
    private String msg;

    public AlreadyExistUserException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
