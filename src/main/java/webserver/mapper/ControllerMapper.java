package webserver.mapper;

import webserver.controller.Controller;
import webserver.controller.MyController;
import webserver.io.ControllerHandler;

import java.lang.reflect.Method;

public class ControllerMapper {

  public void findAppropriateControllerMethod(ControllerHandler handler, String uri){
    MyController myController = new MyController();
    Method[] methods = myController.getClass().getDeclaredMethods();
    for (Method m : methods){
      Controller annotation = m.getAnnotation(Controller.class);
      if(annotation!=null && annotation.uri().equals(uri)){
        handler.setByControllerMapper(myController,m);
        return;
      }
    }
    throw new ControllerMapperException();
  }
  public static class ControllerMapperException extends RuntimeException{
  }
}
