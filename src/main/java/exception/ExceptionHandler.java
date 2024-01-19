package exception;

import model.CommonResponse;

public class ExceptionHandler {
    private static CommonResponse handleGeneralException(SourceException e){
        return CommonResponse.onFail(e.getErrorCode().getStatus(), e.getMessage());
    }
}
