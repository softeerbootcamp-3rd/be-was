package webserver;

import annotation.GetMapping;
import annotation.PostMapping;
import annotation.RequestParam;
import dto.Response;
import util.ControllerMapper;
import util.ResourceLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class RequestMappingHandler {
    private static final String FILE_PATH = "src/main/resources";

    // RequestHandler에서 컨트롤러 전송
    public static Response handleRequest(HttpRequest request) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        if (request.getPath().contains(".") || request.getPath().equals("/")) {
            return handleStaticResource(request);
        }

        Class<?> controllerClass = ControllerMapper.getController(request.getPath());
        Method method = null;

        if (request.getHttpMethod().equals("GET")) {
            method = findGETMethod(controllerClass, request.getPath());
        } else if (request.getHttpMethod().equals("POST")) {
            method = findPOSTMethod(controllerClass, request.getPath());
        }

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
            byte[] body = readAllBytes(file);
            return new Response(HttpStatus.OK, contentType, body);
        }

        return new Response.Builder().httpStatus(HttpStatus.NOT_FOUND).build();
    }

    private static byte[] readAllBytes(File file) throws IOException {
        try (FileInputStream fis = new FileInputStream(file); ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            int size = (int) file.length();
            byte[] buffer = new byte[size];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            return bos.toByteArray();
        }
    }

    private static Method findGETMethod(Class<?> controllerClass, String path) {
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

    private static Method findPOSTMethod(Class<?> controllerClass, String path) {
        Method[] methods = RequestHandlerRegistry.getMethodsForController(controllerClass);
        for (Method method : methods) {
            if (method.isAnnotationPresent(PostMapping.class)) {
                PostMapping postMapping = method.getAnnotation(PostMapping.class);
                if (postMapping.path().equals(path))
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
