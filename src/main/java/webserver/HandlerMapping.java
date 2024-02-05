package webserver;

import annotation.GetMapping;
import annotation.PostMapping;
import controller.Controller;
import http.HttpRequest;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
    public static Map<String, Method> GETMap = new HashMap<>();
    public static Map<String, Method> POSTMap = new HashMap<>();
    static{
        Method[] methods = Controller.class.getMethods();
        for (Method method : methods) {
            if(method.isAnnotationPresent(GetMapping.class)){
                GETMap.put(method.getAnnotation(GetMapping.class).value(), method);
            }else if(method.isAnnotationPresent(PostMapping.class)){
                POSTMap.put(method.getAnnotation(PostMapping.class).value(), method);
            }
        }
    }

    private HandlerMapping() {
    }

    public static Method getMethodMapped(HttpRequest request){
        if(request.isGetMethod()){
            return GETMap.get(request.getPath());
        }else{
            return POSTMap.get(request.getPath());
        }
    }
}
