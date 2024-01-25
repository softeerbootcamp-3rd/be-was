package utils;

import annotations.GetMapping;
import annotations.PostMapping;
import http.HttpRequest;
import http.HttpResponse;
import http.header.ResponseHeader;
import http.status.HttpStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class MethodMapper {
    private static final Logger logger = LoggerFactory.getLogger(MethodMapper.class);
    private static Map<String, Method> getMapper = new HashMap<>();
    private static Map<String, Method> postMapper = new HashMap<>();

    static {
        try {
            File file = new File("src/main/java/controller");
            File[] controllers = file.listFiles();
            if (controllers != null) {
                for (File controller : controllers) {
                    // controller 내의 모든 컨트롤러의 메소드를 가져온다.
                    String className = controller.getName().replace(".java", "");
                    Class<?> cl = Class.forName("controller." +className);

                    for (Method method : cl.getDeclaredMethods()) {
                        if (method.isAnnotationPresent(GetMapping.class))
                            getMapper.put("GET " + method.getAnnotation(GetMapping.class).route(), method);
                        if (method.isAnnotationPresent(PostMapping.class))
                            postMapper.put("POST " + method.getAnnotation(PostMapping.class).route(), method);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("error in getting controller method: " + e.getMessage());
        }
    }

    public static Method findMethodByRequest(HttpRequest request) {
        String httpMethod = request.getMethod();
        String url = request.getUrl();

        if (httpMethod.equals("GET"))
            return getMapper.getOrDefault("GET " + url, null);
        if (httpMethod.equals("POST"))
            return postMapper.getOrDefault("POST " + url, null);
        return null;
    }

    public static void invokeMethod(Method method, HttpRequest request, HttpResponse response) {
        try {
            method.invoke(method.getDeclaringClass().getDeclaredConstructor().newInstance(), request, response);
        } catch (Exception e) {
            response.setHeader(
                    ResponseHeader.of(HttpStatus.INTERNAL_SERVER_ERROR, Map.of())
            );
            response.setEmptyBody();
        }
    }
}
