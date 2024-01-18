package webserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.HashMap;

public class ExceptionResolver {
  private final GlobalExceptionHandler exceptionHandler = new GlobalExceptionHandler();
  private final Logger logger = LoggerFactory.getLogger(ExceptionResolver.class);
  private HashMap<Class<? extends RuntimeException>, Method> mappedMethod = new HashMap<>();

  public ExceptionResolver(){
    Method[] methods = exceptionHandler.getClass().getDeclaredMethods();
    for (Method m : methods){
      ExceptionHandler annotation = m.getAnnotation(ExceptionHandler.class);
      if(annotation!=null){
        mappedMethod.put(annotation.value(),m);
      }
    }
  }

  public MyHttpServletResponse resolve(Exception e){
    MyHttpServletResponse response = null;
    Method handleMethod = mappedMethod.get(e.getClass());
    try {
      if(handleMethod!=null)
        response=(MyHttpServletResponse) handleMethod.invoke(exceptionHandler,e);
    }catch (Exception resolverException){
      logger.error(resolverException.getMessage());
    }
    if(response==null)
      response=new MyHttpServletResponse();
    return response;
  }
}
