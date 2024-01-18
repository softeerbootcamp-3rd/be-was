package handler;

import annotation.GetMapping;
import annotation.RequestParam;
import dto.ResponseBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.HttpRequest;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.Map;

public class RequestMappingHandler {

    // RequestHandler에서 컨트롤러 전송
    public static ResponseBuilder handleRequest(Class<?> controllerClass, HttpRequest request) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException {
        Method method = findMethod(controllerClass, request.getPath());
        Object[] params = createParams(method, request.getParams());

        return invokeMethod(method, params);
    }

    private static Method findMethod(Class<?> controllerClass, String path) {
        Method[] methods = RequestHandlerRegistry.getMethodsForController(controllerClass);
        for (Method method : methods) {
            if (method.isAnnotationPresent(GetMapping.class)) {
                GetMapping getMapping = method.getAnnotation(GetMapping.class);
                if (getMapping.path().equals(path))
                    return method;
            }
        }
        return null;
    }

    private static ResponseBuilder invokeMethod(Method method, Object[] params) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> c = method.getDeclaringClass();
        Object instance = c.getDeclaredConstructor().newInstance();

        return (ResponseBuilder) method.invoke(instance, params);
    }


    private static Object[] createParams(Method method, Map<String, String> originParams){
        Class<? extends Annotation> requestParam = RequestParam.class;
        Parameter[] parameters = method.getParameters();

        Object[] params = new Object[parameters.length];
        int index = 0;

        for(Parameter parameter: parameters){
            if(parameter.isAnnotationPresent(requestParam)){
                RequestParam annotation = (RequestParam) parameter.getAnnotation(requestParam);

                params[index++]= originParams.get(annotation.name());
            }
        }

        return params;
    }


    // 캐싱
    private static class RequestHandlerRegistry {
        private static final Map<Class<?>, Method[]> controllerMethodsMap = new HashMap<>();

        public static synchronized Method[] getMethodsForController(Class<?> controllerClass) {
            if (!controllerMethodsMap.containsKey(controllerClass)) {
                // 스캔 및 메서드 정보 저장
                Method[] methods = controllerClass.getDeclaredMethods();
                controllerMethodsMap.put(controllerClass, methods);
            }
            return controllerMethodsMap.get(controllerClass);
        }
    }

}
