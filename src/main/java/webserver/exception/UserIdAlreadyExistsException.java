package webserver.exception;

import webserver.status.ErrorCode;

public class UserIdAlreadyExistsException extends GeneralException{
    public UserIdAlreadyExistsException(ErrorCode errorCode) {
        super(errorCode);
    }
}
