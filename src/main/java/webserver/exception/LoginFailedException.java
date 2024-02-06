package webserver.exception;

import webserver.status.ErrorCode;

public class LoginFailedException extends GeneralException{
    public LoginFailedException(ErrorCode errorCode) {
        super(errorCode);
    }
}
