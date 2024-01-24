package webserver.adaptor;

import annotation.*;
import controller.BasicController;
import exception.GlobalExceptionHandler;
import exception.InternalServerException;
import exception.ResourceNotFoundException;
import http.Request;
import http.Response;
import controller.RequestController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.ModelAndView;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RequestHandlerAdapter implements HandlerAdapter {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandlerAdapter.class);
    @Override
    public boolean supports(BasicController handler) {
        return (handler instanceof RequestController);
    }

    @Override
    public ModelAndView handle(Request req, Response res, BasicController handler) {
        RequestController controller = (RequestController) handler;

        Map<String, String> model = new HashMap<>();
        String viewName;
        try {
            viewName = process(controller, req, model);

        } catch (Exception e) {
            viewName = GlobalExceptionHandler.handle(e, res);
        }
        ModelAndView mv = new ModelAndView(viewName);
        mv.setModel(model);

        return mv;
    }

        private String process(RequestController controller, Request req, Map<String, String> model){
            try {
                Class<?> clazz = controller.getClass();
                String prefix =clazz.getAnnotation(RequestMapping.class).value();
                String subPath = req.getUrl().replace(prefix, "");
                Method[] methods = clazz.getMethods();
                Method target = null;
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
            } catch (Exception e){
                e.printStackTrace();
                throw new InternalServerException("메서드 실행 실패");
            }
        }

}
