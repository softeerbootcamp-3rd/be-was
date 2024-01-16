package webserver.io;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ControllerHandler {
  private Object controllerInstance;
  private Method method;
  private Object[] args;

  public String handleController() {
    String resourcePath="";
    try {
      resourcePath = (String) method.invoke(controllerInstance,args);
    }catch (InvocationTargetException te){

    }catch (IllegalAccessException ae){

    }
    return resourcePath;
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
