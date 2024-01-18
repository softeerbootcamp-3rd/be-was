package webserver.exception;

import webserver.MyHttpServletResponse;
import webserver.controller.InvalidParameterException;

public class GlobalExceptionHandler {
  @ExceptionHandler(InvalidParameterException.class)
  public MyHttpServletResponse invalidParameterHandler(InvalidParameterException e){
    return null;
  }
}
