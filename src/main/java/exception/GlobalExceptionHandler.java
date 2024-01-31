package exception;

import annotation.ExceptionHandler;
import http.HttpStatus;
import http.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

public class GlobalExceptionHandler{
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    public static String handle(Exception exception, Response res){
        Class<?> clazz = GlobalExceptionHandler.class;

        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if(method.isAnnotationPresent(ExceptionHandler.class)){
                if((method.getAnnotation(ExceptionHandler.class).value()).equals(exception.getClass())){
                    try {
                        return (String) method.invoke(new GlobalExceptionHandler(), res);
                    } catch (Exception e){
                        logger.error(String.valueOf(e.getCause()));
                        return "";
                    }
                }
            }
        }
        return "";
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public String error404(Response res){
        logger.debug("error404");
        res.setStatus(HttpStatus.NOT_FOUND);
        return "redirect:/not-found.html";
    }

    @ExceptionHandler(InternalServerException.class)
    public String error500(Response res){
        logger.debug("error500");
        res.setStatus(HttpStatus.NOT_FOUND);
        return "redirect:/server-error.html";
    }
    @ExceptionHandler(UnAuthorizedException.class)
    public String error403(Response res){
        logger.debug("error403");
        res.setStatus(HttpStatus.UNAUTHORIZED);
        return "redirect:/user/login";
    }
}
