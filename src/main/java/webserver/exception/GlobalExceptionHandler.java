package webserver;

import webserver.controller.InvalidParameterException;

public class GlobalExceptionHandler {
  @ExceptionHandler(InvalidParameterException.class)
  public MyHttpServletResponse invalidParameterHandler(InvalidParameterException e){
    return null;
  }
}
