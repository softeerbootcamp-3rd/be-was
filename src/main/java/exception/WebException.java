package exception;

import util.http.HttpStatus;

public class WebException extends RuntimeException {
    private final int status;
    private final String message;

    public WebException(HttpStatus httpStatus){
        status = httpStatus.value();
        message = httpStatus.getReasonPhrase();
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
