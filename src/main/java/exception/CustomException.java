package exception;

public class CustomException extends Exception {
    private final ErrorCode errorCode;
    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        printStackTrace();
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
