package controller;

import annotation.RequestMapping;
import http.HttpRequest;
import http.HttpResponse;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class FrontController {
    private final Map<String, Controller> controllerMap; //controller list
    private final String RESOURCES_TEMPLATES_URL = "src/main/resources/templates";
    private final String RESOURCES_STATIC_URL = "src/main/resources/static";

    public FrontController() {
        controllerMap = new HashMap<>();
        controllerMap.put("/user", new UserController());
    }

    public void process(HttpRequest request, HttpResponse response) throws InvocationTargetException, IllegalAccessException {
        String url = request.getUrl();

        String path = null;

        if (url.contains(".")) {
            if (url.endsWith(".html")) {
                path = RESOURCES_TEMPLATES_URL + url;
            } else { //.js .css .g..
                path = RESOURCES_STATIC_URL + url;
            }
        } else {
            String[] urlToken = url.split("/");
            Controller controller = controllerMap.get("/"+urlToken[1]);
            Method method = findMethod(controller, url,request.getMethod());
            path = (String)method.invoke(controller, request, response);
            path += ".html";
        }

        response.setPath(path);
    }

    public Method findMethod(Controller controller, String url, String method) {

        Class<?> clazz = controller.getClass();
        for (Method m : clazz.getDeclaredMethods()) {
            if (m.isAnnotationPresent(RequestMapping.class)) {
                RequestMapping requestMappingAnnotation = m.getAnnotation(RequestMapping.class);
                if (url.equals(requestMappingAnnotation.value()) && method.equals(requestMappingAnnotation.method())) {
                    return m;
                }
            }
        }
        return null;
    }
}