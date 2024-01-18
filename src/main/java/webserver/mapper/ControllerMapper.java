package webserver.mapper;

import webserver.controller.Controller;
import webserver.controller.MyController;
import webserver.io.ControllerHandler;

import java.lang.reflect.Method;
import java.util.HashMap;

public class ControllerMapper {
  private final HashMap<String,Method> controllerMethodMap = new HashMap<>();
  public ControllerMapper(){
    //MyController가 가진 모든 method 획득
    Method[] methods = MyController.class.getDeclaredMethods();
    for (Method m : methods){
      Controller annotation = m.getAnnotation(Controller.class);
      if(annotation==null)
        continue;
      String uri = annotation.uri();
      controllerMethodMap.put(uri,m);
    }
  }

  public void findAppropriateControllerMethod(ControllerHandler handler, String uri){
    MyController myController = new MyController();
    Method controllerMethod = controllerMethodMap.get(uri);
    if(controllerMethod==null)
      throw new ControllerMapperException();
    handler.setByControllerMapper(myController,controllerMethod);
  }
  public static class ControllerMapperException extends RuntimeException{
  }
}
