package webserver.adaptor;

import annotation.*;
import controller.BasicController;
import exception.GlobalExceptionHandler;
import exception.InternalServerException;
import exception.UnAuthorizedException;
import http.Request;
import http.Response;
import interceptor.Intercepter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.Model;
import webserver.ModelAndView;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class RequestHandlerAdapter implements HandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerAdapter.class);
    private final Intercepter intercepter = new Intercepter();

    @Override
    public boolean supports(BasicController handler) {
        return (handler instanceof BasicController);
    }

    @Override
    public ModelAndView handle(Request req, Response res, BasicController handler) {


        BasicController controller = (BasicController) handler;
        Model model = new Model();
        String viewName;
        try {
            intercepter.preHandle(req, model);
            viewName = process(controller, req, res,model);

        } catch (Exception e) {
            viewName = GlobalExceptionHandler.handle(e, res);
        }
        ModelAndView mv = new ModelAndView(viewName);

        mv.setModel(model);

        return mv;
    }

        private String process(BasicController controller, Request req, Response res, Model model){
            try {
                Class<?> clazz = controller.getClass();
                String prefix =clazz.getAnnotation(RequestMapping.class).value();
                String subPath = req.getUrl().replace(prefix, "");
                Method[] methods = clazz.getMethods();
                Method target = null;
                logger.debug("clazz = {}",clazz);
                logger.debug("subPath= {}",subPath);
                for (Method method : methods) {
                    if (req.getMethod().equals("GET") && method.isAnnotationPresent(GetMapping.class)) {

                        if ((method.getAnnotation(GetMapping.class).url()).equals(subPath)) {
                            target = method;
                            break;
                        }
                    } else if (req.getMethod().equals("POST") && method.isAnnotationPresent(PostMapping.class)) {
                        if ((method.getAnnotation(PostMapping.class).url()).equals(subPath)) {
                            target = method;
                            break;
                        }
                    }
                }
                Class<?>[] parameterTypes = target.getParameterTypes();
                Object[] parameters = new Object[parameterTypes.length];

                for (int i = 0; i < parameterTypes.length; i++) {
                    if(parameterTypes[i] == Response.class){
                        parameters[i] = res;
                    }
                    else if(parameterTypes[i] == Request.class){
                        parameters[i] = req;
                    }else if(parameterTypes[i] == Model.class){
                        parameters[i] = model;
                    }
                    Annotation[] annotations = target.getParameterAnnotations()[i];
                    for (Annotation annotation : annotations) {
                        if (annotation.annotationType() == RequestParam.class) {
                            String paramName = ((RequestParam) annotation).name();
                            parameters[i] = req.getRequestParam().get(paramName);
                        }
                        else if(annotation.annotationType() == RequestBody.class){
                            Constructor<?> constructor = parameterTypes[i].getDeclaredConstructor();
                            Object parameterInstance = constructor.newInstance();

                            Field[] fields = parameterTypes[i].getDeclaredFields();
                            for (Field field : fields) {
                                String fieldName = field.getName();
                                if (req.getBody().containsKey(fieldName)) {
                                    String setterMethodName = "set" + Character.toUpperCase(fieldName.charAt(0)) + fieldName.substring(1);
                                    Method setterMethod = parameterTypes[i].getMethod(setterMethodName, field.getType());
                                    setterMethod.invoke(parameterInstance, req.getBody().get(fieldName));
                                }
                            }
                            parameters[i] = parameterInstance;
                        }
                    }
                }


                return (String) target.invoke(controller, parameters);
            }catch (UnAuthorizedException e){
                throw new UnAuthorizedException("로그인 정보 없음");
            }
            catch (Exception e){
                e.printStackTrace();
                throw new InternalServerException("메서드 실행 실패");
            }
        }

}
