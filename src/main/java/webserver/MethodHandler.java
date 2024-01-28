package webserver;

import annotation.GetMapping;
import annotation.PostMapping;
import controller.*;
import request.HttpRequest;
import response.HttpResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MethodHandler {
    private static final List<Class<?>> controllers = new ArrayList<>();

    static {
        controllers.add(MemberJoinController.class);
        controllers.add(MemberListController.class);
        controllers.add(MemberLoginController.class);
        controllers.add(MemberLogoutController.class);
        controllers.add(ResourceController.class);
    }

    public void process(HttpRequest request, HttpResponse response) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Method method = findTargetMethod(request.getMethod(), request.getUrl());
        Object instance = method.getDeclaringClass().getConstructor().newInstance();
        method.invoke(instance, request, response);
    }

    private Method findTargetMethod(String requestMethod, String requestUrl) {
        if(requestUrl.contains(".")) {
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



}
