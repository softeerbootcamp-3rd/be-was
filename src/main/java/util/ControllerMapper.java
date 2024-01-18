package util;

import controller.GetController;

import java.util.HashMap;
import java.util.Map;

public class ControllerMapper {

    public static final Map<String, Class<?>> CONTROLLER_MAPPING = new HashMap<>();

    static {
        CONTROLLER_MAPPING.put("GET", GetController.class);
    }

    public static Class<?> getController(String requestMethod) {
        return CONTROLLER_MAPPING.get(requestMethod);
    }

}