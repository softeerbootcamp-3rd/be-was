package webserver.handler;

import webserver.exception.GeneralException;
import webserver.response.Response;
import webserver.status.ErrorCode;
import webserver.type.ContentType;

public class ExceptionHandler {
    public static Response handle(Throwable e){
        if(e instanceof GeneralException){
            return handleGeneralException((GeneralException) e);
        } else{
            return handleException();
        }
    }

    private static Response handleGeneralException(GeneralException e){
        return Response.onFailure(e.getErrorCode().getHttpStatus(), ContentType.HTML, e.getMessage().getBytes());
    }

    private static Response handleException(){
        ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;

        return Response.onFailure(errorCode.getHttpStatus(), ContentType.HTML, errorCode.getMessage().getBytes());
    }
}
