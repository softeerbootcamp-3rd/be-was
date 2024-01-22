package util;

import controller.UserController;

import java.util.HashMap;
import java.util.Map;

public class ControllerMapper {

    public static final Map<String, Class<?>> CONTROLLER_MAP = new HashMap<>();

    static {
        CONTROLLER_MAP.put("user", UserController.class);
    }

    public static Class<?> getController(String path) {
        String mappingPath = path.split("/")[0];
        return CONTROLLER_MAP.get(mappingPath);
    }

}