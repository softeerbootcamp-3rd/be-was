package webserver;

import annotation.GetMapping;
import annotation.RequestParam;
import dto.Response;
import util.ResourceLoader;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestMappingHandler {
    private static final String FILE_PATH = "src/main/resources";

    // RequestHandler에서 컨트롤러 전송
    public static Response handleRequest(Class<?> controllerClass, HttpRequest request) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        if (request.getPath().contains(".") || request.getPath().equals("/")) {
            return handleStaticResource(request);
        }

        Method method = findMethod(controllerClass, request.getPath());
        if (method == null) {
            return new Response.Builder().httpStatus(HttpStatus.NOT_FOUND).build();
        }

        Object[] params = createParams(method, request.getParams());
        return invokeMethod(method, params);
    }

    private static Response handleStaticResource(HttpRequest request) throws IOException {
        String path = request.getPath().equals("/") ? "/index.html" : request.getPath();
        String contentType = ResourceLoader.getContentType(path);
        String filePath = FILE_PATH + "/static" + path;

        if (path.endsWith(".html")) {
            filePath = FILE_PATH + "/templates" + path;
        }


        File file = new File(filePath);

        if (file.exists()) {
            byte[] body = Files.readAllBytes(file.toPath());
            return new Response(HttpStatus.OK, contentType, body);
        }

        return new Response.Builder().httpStatus(HttpStatus.NOT_FOUND).build();
    }

    private static Method findMethod(Class<?> controllerClass, String path) {
        // [ 피드백 ] 아예 처음부터 맵핑해놓고 시작하기
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

    private static Response invokeMethod(Method method, Object[] params) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> c = method.getDeclaringClass();
        Object instance = c.getDeclaredConstructor().newInstance();

        return (Response) method.invoke(instance, params);
    }


    private static Object[] createParams(Method method, Map<String, String> originParams){
        Class<? extends Annotation> requestParam = RequestParam.class;
        Parameter[] parameters = method.getParameters();

        Object[] params = new Object[parameters.length];
        int index = 0;

        for (Parameter parameter : parameters) {
            if (parameter.isAnnotationPresent(requestParam)) {
                RequestParam annotation = (RequestParam) parameter.getAnnotation(requestParam);

                String paramName = annotation.name();
                Class<?> paramType = parameter.getType();

                // 각 타입에 맞게 변환 처리
                if (paramType == String.class) {
                    params[index++] = originParams.get(paramName);
                } else if (paramType == Integer.class || paramType == int.class) {
                    params[index++] = Integer.parseInt(originParams.get(paramName));
                } else if (paramType == Double.class || paramType == double.class) {
                    params[index++] = Double.parseDouble(originParams.get(paramName));
                } else if (paramType == Long.class || paramType == long.class) {
                    params[index++] = Long.parseLong(originParams.get(paramName));
                } else if (paramType == Boolean.class || paramType == boolean.class) {
                    params[index++] = Boolean.parseBoolean(originParams.get(paramName));
                }
            }
        }

        return params;
    }


    // 캐싱
    private static class RequestHandlerRegistry {
        private static final Map<Class<?>, Method[]> controllerMethodsMap = new ConcurrentHashMap<>();

        // synchronized 대신 ConcurrentHashMap 사용
        public static Method[] getMethodsForController(Class<?> controllerClass) {
            if (!controllerMethodsMap.containsKey(controllerClass)) {
                // 스캔 및 메서드 정보 저장
                Method[] methods = controllerClass.getDeclaredMethods();
                controllerMethodsMap.put(controllerClass, methods);
            }
            return controllerMethodsMap.get(controllerClass);
        }
    }

}
