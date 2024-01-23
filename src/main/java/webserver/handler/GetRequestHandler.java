package webserver.handler;

import webserver.annotation.GetMapping;
import webserver.annotation.RequestParam;
import controller.TestController;
import controller.UserController;
import webserver.exception.GeneralException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.request.Request;
import webserver.status.ErrorCode;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class GetRequestHandler {
    private static final Logger logger = LoggerFactory.getLogger(GetRequestHandler.class);

    public static Object run(Request request) throws Throwable {
        List<Class<?>> classes = getControllerClasses();
        Method method = findMethod(classes, request.getPath());

        if(method == null){
            throw new GeneralException(ErrorCode.RESOURCE_NOT_FOUND_ERROR);
        }

        Object[] params = createParams(method, request);

        try {
            return executeMethod(method, params);
        } catch (InvocationTargetException e){
            throw e.getTargetException();
        }
    }

    private static Object executeMethod(Method method, Object[] params) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> c = method.getDeclaringClass();
        Object instance = c.getDeclaredConstructor().newInstance();

        return method.invoke(instance, params);
    }

    private static Object[] createParams(Method method, Request request){
        Class<? extends Annotation> requestParam = RequestParam.class;
        Parameter[] parameters = method.getParameters();

        Object[] params = new Object[parameters.length];
        int index = 0;

        for(Parameter parameter: parameters){
            if(parameter.isAnnotationPresent(requestParam)){
                RequestParam annotation = (RequestParam) parameter.getAnnotation(requestParam);
                params[index++]= request.getParam(annotation.name());
            }

            if(params[index - 1] == null){
                logger.error(parameter.getName());
                throw new GeneralException(ErrorCode.ILLEGAL_ARGUMENT_ERROR);
            }
        }

        return params;
    }

    private static Method findMethod(List<Class<?>> classes, String path){
        Class<? extends Annotation> getMapping = GetMapping.class;

        for(Class<?> c: classes){
            Method[] methods = c.getDeclaredMethods();

            for(Method method: methods){
                if(method.isAnnotationPresent(getMapping)){
                    GetMapping annotation = (GetMapping) method.getAnnotation(getMapping);

                    if(path.equals(annotation.path())){
                        return method;
                    }
                }
            }
        }

        return null;
    }

    private static List<Class<?>> getControllerClasses(){
        List<Class<?>> classes = new ArrayList<>();

        classes.add(UserController.class);
        classes.add(TestController.class);

        return classes;
    }
}
