package webserver.io;

import webserver.MyHttpServletResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ControllerHandler {
  private Object controllerInstance;
  private Method method;
  private Object[] args;

  public MyHttpServletResponse handleController() throws InvocationTargetException, IllegalAccessException {
    String controllerReturnValue="";
    controllerReturnValue = (String) method.invoke(controllerInstance, args);
    return new MyHttpServletResponse(controllerReturnValue);
  }

  public void setByControllerMapper(Object controllerInstance,Method method){
    this.controllerInstance=controllerInstance;
    this.method=method;
  }
  public void setByParameterMapper(Object[] args){
    this.args=args;
  }
  public Method getMethod(){
    return this.method;
  }
}
