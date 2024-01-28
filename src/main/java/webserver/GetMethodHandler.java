package webserver;

import annotation.GetMapping;
import controller.ResourceController;
import request.HttpRequest;
import response.HttpResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class GetMethodHandler {
    private static final List<Class<?>> controllers = new ArrayList<>();

    static {
        controllers.add(ResourceController.class);
    }

    public void process(HttpRequest request, HttpResponse response) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Method method = findMethod(request.getUrl());
        Object instance = method.getDeclaringClass().getConstructor().newInstance();
        method.invoke(instance, request, response);
    }

    private Method findMethod(String url) {
        for (Class<?> c : controllers) {
            Method[] methods = c.getDeclaredMethods();
            for (Method method : methods) {
                if (method.isAnnotationPresent(GetMapping.class)) {
                    GetMapping getMapping = method.getAnnotation(GetMapping.class);
                    if (getMapping.url().equals(url)) {
                        return method;
                    }
                }
            }
        }
        return null;
    }



}
