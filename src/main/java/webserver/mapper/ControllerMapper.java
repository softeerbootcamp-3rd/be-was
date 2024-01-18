package webserver.mapper;

import webserver.controller.Controller;
import webserver.controller.MyController;
import webserver.io.ControllerHandler;

import java.lang.reflect.Method;

public class ControllerMapper {

  public void findAppropriateControllerMethod(ControllerHandler handler, String uri){
    MyController myController = new MyController();
    //MyController가 가진 모든 method 획득
    Method[] methods = myController.getClass().getDeclaredMethods();
    for (Method m : methods){
      Controller annotation = m.getAnnotation(Controller.class);
      //메서드가 @Controller를 가지고 있고 http요청 uri가 @Controller내부의 uri value와 일치하는지 검사
      if(annotation!=null && annotation.uri().equals(uri)){
        //일치하면 해당 method를 controller method로 선택.
        handler.setByControllerMapper(myController,m);
        return;
      }
    }
    throw new ControllerMapperException();
  }
  public static class ControllerMapperException extends RuntimeException{
  }
}
