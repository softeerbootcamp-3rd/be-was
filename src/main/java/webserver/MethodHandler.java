package webserver;

import annotation.GetMapping;
import annotation.PostMapping;
import annotation.RequestParam;
import controller.*;
import request.HttpRequest;
import response.HttpResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MethodHandler {
    private static final List<Class<?>> controllers = new ArrayList<>();

    static {
        controllers.add(UserController.class);
        controllers.add(ResourceController.class);
    }

    public void process(HttpRequest request, HttpResponse response) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Method method = findTargetMethod(request.getMethod(), request.getUrl());
        Object instance = method.getDeclaringClass().getConstructor().newInstance();

        Map<String, String> params = request.getParams();
        if(params.size() == 0) {
            method.invoke(instance, request);
        } else {
            Object[] objects = bindParameters(method, params);
            method.invoke(instance, objects);
        }
    }

    private Method findTargetMethod(String requestMethod, String requestUrl) {
        if(requestUrl.contains(".") && !requestUrl.equals("/user/list.html")) {
            requestUrl = "/resources";
        }
        for (Class<?> c : controllers) {
            Method[] methods = c.getDeclaredMethods();
            for (Method method : methods) {

                if (requestMethod.equals("GET") && method.isAnnotationPresent(GetMapping.class)) {
                    GetMapping getMapping = method.getAnnotation(GetMapping.class);
                    if (getMapping.url().equals(requestUrl)) {
                        return method;
                    }
                }

                if (requestMethod.equals("POST") && method.isAnnotationPresent(PostMapping.class)) {
                    PostMapping postMapping = method.getAnnotation(PostMapping.class);
                    if (postMapping.url().equals(requestUrl)) {
                        return method;
                    }
                }
            }
        }
        return null;
    }

    private Object[] bindParameters(Method method, Map<String, String> params) {
        Parameter[] parameters = method.getParameters();
        Object[] boundParams = new Object[parameters.length];

        for (int i=0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            if (parameter.isAnnotationPresent(RequestParam.class)) {
                RequestParam requestParam = parameter.getAnnotation(RequestParam.class);
                String paramName = requestParam.name();
                String paramValue = params.get(paramName);

                boundParams[i] = paramValue;
            }
        }

        return boundParams;
    }

}
