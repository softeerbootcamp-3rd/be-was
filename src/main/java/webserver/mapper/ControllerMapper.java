package webserver.mapper;

import webserver.controller.GetMapping;
import webserver.controller.MyController;
import webserver.controller.PostMapping;
import webserver.handler.ControllerHandler;

import java.lang.reflect.Method;
import java.util.HashMap;

public class ControllerMapper {
  private final HashMap<String,Method> getMethodMap = new HashMap<>();
  private final HashMap<String,Method> postMethodMap = new HashMap<>();
  public ControllerMapper(){
    //MyController가 가진 모든 method 획득
    Method[] methods = MyController.class.getDeclaredMethods();
    for (Method m : methods){
      GetMapping getAnnotation = m.getAnnotation(GetMapping.class);
      if(getAnnotation!=null) {
        String uri = getAnnotation.uri();
        getMethodMap.put(uri, m);
      }
      PostMapping postAnnotation = m.getAnnotation(PostMapping.class);
      if(postAnnotation!=null){
        String uri = postAnnotation.uri();
        postMethodMap.put(uri, m);
      }
    }
  }

  public void findAppropriateControllerMethod(ControllerHandler handler, String uri, String httpMethod){
    MyController myController = new MyController();
    Method controllerMethod = null;
    if(httpMethod.equals("GET")){
      controllerMethod=getMethodMap.get(uri);
    }else if(httpMethod.equals("POST")){
      controllerMethod= postMethodMap.get(uri);
    }
    if(controllerMethod==null)
      throw new ControllerMapperException();
    handler.setByControllerMapper(myController,controllerMethod);
  }
  public static class ControllerMapperException extends RuntimeException{
  }
}
