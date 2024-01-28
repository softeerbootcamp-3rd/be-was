package util;

import annotation.GetMapping;
import annotation.PostMapping;
import annotation.RequestMapping;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

import static config.AppConfig.userController;

public class UrlControllerMapper {
    private static class UrlControllerMapperHolder{
        private static final UrlControllerMapper INSTANCE = new UrlControllerMapper();
    }
    private final HashMap<String, Method> urlMapper = new HashMap<>();
    private final HashMap<String, Object> controllerMapper = new HashMap<>();

    public static UrlControllerMapper getInstance(){
        return UrlControllerMapperHolder.INSTANCE;
    }
    public UrlControllerMapper() {
        registerController(userController());
    }
    public void registerController(Object controller) {
        Class<?> controllerClass = controller.getClass();
        controllerMapper.put(controllerClass.getName(), controller);
        processControllerMethods(controllerClass);
    }

    private void processControllerMethods(Class<?> controllerClass) {
        if (controllerClass.isAnnotationPresent(RequestMapping.class)) {
            RequestMapping classAnnotation = controllerClass.getAnnotation(RequestMapping.class);
            String baseUrl = classAnnotation.value();
            Method[] methods = controllerClass.getDeclaredMethods();
            for (Method method : methods) {
                processControllerMethodAnnotations(method, baseUrl);
            }
        }
    }

    private void processControllerMethodAnnotations(Method method, String baseUrl) {
        if (method.isAnnotationPresent(GetMapping.class) || method.isAnnotationPresent(PostMapping.class)) {
            String url = baseUrl + getRequestMappingValue(method);
            urlMapper.put(url, method);
        }
    }

    private String getRequestMappingValue(Method method) {
        RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);
        if (requestMapping != null) {
            return requestMapping.value();
        }
        GetMapping getMapping = method.getDeclaredAnnotation(GetMapping.class);
        if (getMapping != null) {
            return getMapping.value();
        }
        PostMapping postMapping = method.getDeclaredAnnotation(PostMapping.class);
        if (postMapping != null) {
            return postMapping.value();
        }
        return "";
    }

    public Method getMethod(String pathUrl) {
        return urlMapper.get(pathUrl);
    }

    public Object getController(String pathUrl) {
        return controllerMapper.get(pathUrl);
    }
    public Set<String> getMappingUrls(){
        return urlMapper.keySet();
    }
}
