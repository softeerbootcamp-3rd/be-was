package util.web;

import annotation.NotEmpty;
import annotation.RequestBody;
import annotation.RequestMapping;
import annotation.RequestParam;
import constant.HttpStatus;
import model.SharedData;
import util.ClassScanner;
import util.mapper.ObjectMapper;
import webserver.HttpRequest;
import webserver.HttpResponse;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class RequestMapper {
    public static final Map<String, Method> REQUEST_MAP;

    static {
        List<Class<?>> controllers = ClassScanner.scanControllers("controller");
        Map<String, Method> controllerMap = new HashMap<>();

        Class<? extends Annotation> requestMapping = RequestMapping.class;
        for (Class<?> c : controllers) {
            Method[] methods = c.getDeclaredMethods();

            for (Method method : methods) {
                if (method.isAnnotationPresent(requestMapping)) {
                    RequestMapping requestInfo = (RequestMapping) method.getAnnotation(requestMapping);
                    controllerMap.put(requestInfo.method() + " " + requestInfo.path(), method);
                }
            }
        }
        REQUEST_MAP = Map.copyOf(controllerMap);
    }


    public static Method getMethod(HttpRequest request) {
        for (Map.Entry<String, Method> entry : REQUEST_MAP.entrySet()) {
            String mappedPath = entry.getKey();
            String requestPath = request.getMethod() + " " + request.getPath();

            Map<String, String> pathParams = getPathParams(mappedPath, requestPath);
            if (pathParams != null) {
                SharedData.pathParams.set(Map.copyOf(pathParams));
                return entry.getValue();
            }
        }
        return null;
    }

    private static Map<String, String> getPathParams(String mappedPath, String requestPath) {
        // "GET /test/{id}" -> "GET /test/\\d+"
        String regexPath = mappedPath.replaceAll("\\{[^/]+}", "(\\\\d+)");

        Pattern pattern = Pattern.compile(regexPath);
        Matcher matcher = pattern.matcher(requestPath);

        Map<String, String> pathParams = new HashMap<>();
        if (!matcher.matches())
            return null;

        for (int i = 1; i <= matcher.groupCount(); i++) {
            String paramName = extractParamName(mappedPath, i);
            String paramValue = matcher.group(i);
            pathParams.put(paramName, paramValue);
        }
        return pathParams;
    }

    private static String extractParamName(String mappedPath, int groupIndex) {
        Pattern paramNamePattern = Pattern.compile("\\{([^/]+)}");
        Matcher paramNameMatcher = paramNamePattern.matcher(mappedPath);
        int count = 0;

        while (paramNameMatcher.find()) {
            count++;
            if (count == groupIndex) {
                return paramNameMatcher.group(1);
            }
        }

        throw new IllegalArgumentException("No parameter name found for group index: " + groupIndex);
    }

    public static HttpResponse invoke(Method method)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        Class<?> clazz = method.getDeclaringClass();
        Object instance = clazz.getDeclaredConstructor().newInstance();

        Object result = method.invoke(instance, mapParams(method));
        if (result instanceof HttpResponse) return (HttpResponse) result;
        return HttpResponse.of(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private static Object[] mapParams(Method method) {
        Parameter[] parameters = method.getParameters();
        Object[] params = new Object[parameters.length];

        IntStream.range(0, parameters.length)
                .forEach(i -> {
                    Parameter parameter = parameters[i];
                    if (parameter.isAnnotationPresent(RequestParam.class)){
                        // RequestParam - URI의 queryString으로 받는 파라미터
                        RequestParam annotation = parameter.getAnnotation(RequestParam.class);
                        params[i] = ObjectMapper.mapRequestParam(annotation.value(), parameter.getType());
                        checkRequired(params[i], annotation);
                    } else if (parameter.isAnnotationPresent(RequestBody.class)) {
                        // RequestBody - body로 받는 파라미터
                        try {
                            params[i] = RequestParser.parseBody(parameter.getType());
                            checkNotEmpty(params[i], parameter.getType());
                        } catch (InvocationTargetException | IllegalAccessException | NoSuchMethodException |
                                 InstantiationException | IOException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });
        return params;
    }

    private static void checkRequired(Object instance, RequestParam annotation) {
        if (annotation.required() && Objects.isNull(instance))
            throw new RuntimeException("Parameter '" + annotation.value() + "' is null.");
    }

    private static void checkNotEmpty(Object instance, Class<?> clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.isAnnotationPresent(NotEmpty.class)) {
                field.setAccessible(true);
                try {
                    Object value = field.get(instance);
                    if (value == null || value.toString().isEmpty()) {
                        throw new RuntimeException("Field '" + field.getName() + "' is empty.");
                    }
                } catch (IllegalAccessException e) {
                    throw new RuntimeException("Error accessing field '" + field.getName() + "'.", e);
                }
            }
        }
    }
}
