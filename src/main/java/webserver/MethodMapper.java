package webserver;

import annotations.GetMapping;
import annotations.PostMapping;
import webserver.http.HttpRequest;
import webserver.http.HttpResponse;
import webserver.http.HttpStatus;
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
                    if (controller.isDirectory())
                        continue;
                    String className = controller.getName().replace(".java", "");
                    Class<?> cl = Class.forName("controller." + className);

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
            Class<?> cl = method.getDeclaringClass();
            Object instance = cl.getDeclaredMethod("getInstance").invoke(null);
            // 메서드를 invoke할 인스턴스는 싱글톤 패턴으로 작성되었기 때문에
            // 여러 번 메서드를 호출해도 단일한 컨트롤러 인스턴스에서 호출된다.
            method.invoke(instance, request, response);
        } catch (Exception e) {
            logger.debug(e.getClass().toString() + " " + e.getMessage());
            response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
