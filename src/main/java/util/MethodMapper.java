package util;

import annotation.GetMapping;
import annotation.PostMapping;

import java.lang.annotation.Annotation;
import java.util.HashMap;
import java.util.Map;

public class MethodMapper {

    public static final Map<String, Class<? extends Annotation>> METHOD_MAP = new HashMap<>();

    static {
        METHOD_MAP.put("GET", GetMapping.class);
        METHOD_MAP.put("POST", PostMapping.class);
    }

    public static Class<? extends Annotation> getMethodMapping(String httpMethod) {
        return METHOD_MAP.get(httpMethod);
    }

}
