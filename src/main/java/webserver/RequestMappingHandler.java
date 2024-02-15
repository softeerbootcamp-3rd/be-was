package webserver;

import annotation.*;
import db.Database;
import org.checkerframework.checker.units.qual.C;
import util.*;
import webserver.http.*;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class RequestMappingHandler {

    // RequestHandler에서 컨트롤러 전송
    public static ResponseEntity handleRequest(HttpRequest request) throws InvocationTargetException, NoSuchMethodException, InstantiationException, IllegalAccessException, IOException {
        String path = StringParser.parsePurePath(request.getPath());
        if (path.contains(".") || path.equals("/")) {
            return ResourceManager.handleStaticResource(request);
        }

        Class<?> controllerClass = ControllerMapper.getController(path);
        if (controllerClass == null) {
            Map<String, List<String>> headerMap = new HashMap<>();
            headerMap.put(HttpHeader.LOCATION, Collections.singletonList("/error/404.html"));
            return new ResponseEntity<>(HttpStatus.FOUND, headerMap);
        }
        Method method = null;

        if (request.getHttpMethod().equals("GET")) {
            method = findGETMethod(controllerClass, path);
        } else if (request.getHttpMethod().equals("POST")) {
            method = findPOSTMethod(controllerClass, path);
        }

        if (method == null) {
            Map<String, List<String>> headerMap = new HashMap<>();
            headerMap.put(HttpHeader.LOCATION, Collections.singletonList("/error/404.html"));
            return new ResponseEntity<>(HttpStatus.FOUND, headerMap);
        }

        ResponseEntity response = null;
        if (path.equals("/user/logout") || path.equals("/user/name")
            || path.equals("/post/upload")) {
            response = invokeMethod(method, request);
        } else {
            response = invokeMethod(method, createParams(method, request.getParams()));
        }

        response = addHeaders(request, method, response);

        return response;
    }

    private static ResponseEntity addHeaders(HttpRequest request, Method method, ResponseEntity response) {
        // @ResponseBody 어노테이션이 있으면 application/json 타입으로 전송
        if (method.isAnnotationPresent(ResponseBody.class)) {
            Object responseBody = response.getBody();

            // T 타입인 경우
            if (responseBody != null && !isListType(responseBody.getClass())) {
                String body = JsonConverter.convertObjectToJson(responseBody);
                response.getHeaders().setContentType("application/json");
                response.getHeaders().setContentLength(String.valueOf(body.getBytes().length));
                response.setBody(body);
            }

            // List<T> 타입인 경우
            if (responseBody != null && isListType(responseBody.getClass())) {
                String body = JsonConverter.convertListToJson((List<?>) responseBody);
                response.getHeaders().setContentType("application/json");
                response.getHeaders().setContentLength(String.valueOf(body.getBytes().length));
                response.setBody(body);
            }
        }
        return response;
    }

    private static boolean isListType(Class<?> type) {
        return List.class.isAssignableFrom(type);
    }

    private static Method findMethod(Class<?> controllerClass, String path, Class<? extends Annotation> annotationType) {
        Method[] methods = RequestHandlerRegistry.getMethodsForController(controllerClass);
        String basePath = controllerClass.getAnnotation(RequestMapping.class).value();
        for (Method method : methods) {
            if (method.isAnnotationPresent(annotationType)) {
                Annotation mappingAnnotation = method.getAnnotation(annotationType);

                String mappingPath = null;
                try {
                    mappingPath = (String) annotationType.getMethod("path").invoke(mappingAnnotation);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                String controllerPath = basePath + mappingPath;
                if (controllerPath.equals(path))
                    return method;
            }
        }
        return null;
    }
    private static Method findGETMethod(Class<?> controllerClass, String path) {
        return findMethod(controllerClass, path, GetMapping.class);
    }

    private static Method findPOSTMethod(Class<?> controllerClass, String path) {
        return findMethod(controllerClass, path, PostMapping.class);
    }

    private static ResponseEntity invokeMethod(Method method, Object... params) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Class<?> c = method.getDeclaringClass();
        Object instance = c.getDeclaredConstructor().newInstance();
        return (ResponseEntity) method.invoke(instance, params);
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

                if (originParams.get(paramName) == null) {
                    params[index++] = null;
                    continue;
                }

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
