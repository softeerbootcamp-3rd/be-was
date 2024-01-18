package exception;

import constant.ErrorCode;

public class WebServerException extends RuntimeException {
    private ErrorCode errorCode;

    public WebServerException(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }
}
