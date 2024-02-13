package util;

import controller.HttpMethod;
import controller.RequestDataController;
import controller.Route;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MethodMapper {
    public static Map<String, Method> getRouteMap = new HashMap<>();
    public static Map<String, Method> postRouteMap = new HashMap<>();

    static {
        // 라우팅 메서드 등록
        registerRoutes();
    }

    private static void registerRoutes() {
        Method[] methods = RequestDataController.class.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Route.class)) {
                Route route = method.getAnnotation(Route.class);
                String uri = route.uri();
                if (route.method() == HttpMethod.GET) {
                    getRouteMap.put(uri, method);
                } else if (route.method() == HttpMethod.POST) {
                    postRouteMap.put(uri, method);
                }
            }
        }
    }
}
