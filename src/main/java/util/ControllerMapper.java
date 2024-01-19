package util;

import controller.GetController;

import java.util.HashMap;
import java.util.Map;

public class ControllerMapper {

    public static final Map<String, Class<?>> CONTROLLER_MAP = new HashMap<>();

    static {
        CONTROLLER_MAP.put("GET", GetController.class);
    }

    public static Class<?> getController(String requestMethod) {
        return CONTROLLER_MAP.get(requestMethod);
    }

}