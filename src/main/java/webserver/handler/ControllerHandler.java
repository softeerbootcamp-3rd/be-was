package webserver.handler;

import webserver.MyHttpServletRequest;
import webserver.MyHttpServletResponse;
import webserver.handler.Handler;
import webserver.mapper.ControllerMapper;
import webserver.mapper.ParameterMapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ControllerHandler implements Handler {
  private Object controllerInstance;
  private Method method;
  private Object[] args;

  private static final ControllerMapper controllerMapper = new ControllerMapper();
  private static final ParameterMapper parameterMapper = new ParameterMapper();

  @Override
  public MyHttpServletResponse handle(MyHttpServletRequest request){
    //ControllerMapper로부터 요청을 수행할 수 있는 Method를 가져온다.
    controllerMapper.findAppropriateControllerMethod(this, request.getUri());
    //ParameterMapper로부터 Method의 파라미터들을 MyHttpServletRequest가 가지고 있는 쿼리 파라미터에서 추출해 주입한다.
    parameterMapper.findAppropriateParameter(this, request);
    //Method를 실행할 수 있는 조건(실행할 Method, Method에 필요한 파라미터값, 실행할 Method를 가지고 있는 객체)이
    //다 갖춰졌기 때문에 해당 Method를 ControllerHandler가 실행한다.
    return handleController();
  }

  @Override
  public boolean canHandle(MyHttpServletRequest request) {
    return !request.getUri().contains(".") || request.getUri().contains("html");
  }

  public MyHttpServletResponse handleController(){
    String controllerReturnValue="";
    //controller method를 실행하고 string값을 받음 (redirect uri 혹은 resource path)
    try {
      controllerReturnValue = (String) method.invoke(controllerInstance, args);
    }catch (InvocationTargetException targetException){
      if (RuntimeException.class.isAssignableFrom(targetException.getTargetException().getClass()))
        throw (RuntimeException) targetException.getTargetException();
      else
        throw new RuntimeException();
    }catch (IllegalAccessException accessException){
      throw new RuntimeException();
    }
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
