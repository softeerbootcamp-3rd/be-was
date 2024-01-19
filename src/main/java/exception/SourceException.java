package exception;

import util.ErrorCode;

public class SourceException extends RuntimeException{
    private ErrorCode errorCode;
    public SourceException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
