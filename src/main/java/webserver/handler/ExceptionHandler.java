package webserver.handler;

import webserver.exception.GeneralException;
import webserver.exception.LoginFailedException;
import webserver.exception.UserIdAlreadyExistsException;
import webserver.response.Response;
import webserver.status.ErrorCode;

public class ExceptionHandler {
    public static Response handle(Throwable e){
        if(e instanceof UserIdAlreadyExistsException){
            return handleUserIdAlreadyExistsException();
        } else if(e instanceof LoginFailedException){
            return handleLoginFailedException();
        } else if(e instanceof GeneralException){
            return handleGeneralException((GeneralException) e);
        } else{
            return handleException();
        }
    }

    private static Response handleLoginFailedException(){
        return Response.redirect("/user/login_failed.html");
    }

    private static Response handleUserIdAlreadyExistsException(){
        return Response.redirect("/user/form_failed.html");
    }

    private static Response handleGeneralException(GeneralException e){
        return Response.onFailure(e.getErrorCode().getHttpStatus(), e.getMessage().getBytes());
    }

    private static Response handleException(){
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        return Response.onFailure(errorCode.getHttpStatus(), errorCode.getMessage().getBytes());
    }
}
