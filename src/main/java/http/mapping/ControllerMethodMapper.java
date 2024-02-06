package http.mapping;

import http.enums.HttpMethod;
import http.annotation.GetMapping;
import http.annotation.PostMapping;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class ControllerMethodMapper {
    private final Map<RequestMappingInfo, Method> mappings = new HashMap<>();

    public void scan(Object controller) {
        Class<?> clazz = controller.getClass();
        Arrays.stream(clazz.getDeclaredMethods())
                .flatMap(method -> Stream.of(GetMapping.class, PostMapping.class)
                        .filter(annotationClass -> method.isAnnotationPresent(annotationClass))
                        .map(annotationClass -> createMappingInfo(method, annotationClass)))
                .forEach(mappingInfo -> {
                    mappings.put(mappingInfo.getRequestMappingInfo(), mappingInfo.getMethod());
                });
    }

    private RequestMethodMapping createMappingInfo(Method method, Class<? extends Annotation> annotationClass) {
        String path = extractPath(method, annotationClass);
        HttpMethod httpMethod = HttpMethod.resolve(annotationClass);
        return new RequestMethodMapping(new RequestMappingInfo(path, httpMethod), method);
    }

    private String extractPath(Method method, Class<? extends Annotation> annotationClass) {
        return Stream.of(GetMapping.class, PostMapping.class)
                .filter(annotationClass::equals)
                .findFirst()
                .map(annotation -> {
                    try {
                        return (String) method.getAnnotation(annotation).getClass().getMethod("value").invoke(method.getAnnotation(annotation));
                    } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                })
                .orElseThrow(() -> new IllegalArgumentException("Unsupported annotation: " + annotationClass));
    }

    public Method getMappedMethod(RequestMappingInfo requestMappingInfo) {
        Method method = mappings.get(requestMappingInfo);
        return method;
    }
}
