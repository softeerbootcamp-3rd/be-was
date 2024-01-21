package webserver.exception;

import webserver.MyHttpServletResponse;
import webserver.controller.DuplicateUserException;
import webserver.controller.InvalidParameterException;

public class GlobalExceptionHandler {
  @ExceptionHandler(InvalidParameterException.class)
  public MyHttpServletResponse invalidParameterHandler(InvalidParameterException e){
    return new MyHttpServletResponse();
  }

  @ExceptionHandler(DuplicateUserException.class)
  public MyHttpServletResponse handleDuplicateUser(DuplicateUserException e){
    return new MyHttpServletResponse();
  }
}
