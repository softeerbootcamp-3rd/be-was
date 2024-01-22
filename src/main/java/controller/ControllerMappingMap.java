package controller;

import java.util.HashMap;
import java.util.Map;

public class ControllerMappingMap {
    private static final Map<String, Controller> controllerMappingMap = new HashMap<>();

    static {
        controllerMappingMap.put("resource", new ResourceController());
        controllerMappingMap.put("/user/create", new MemberJoinController());
    }

    public static Controller getController(String url) {
        if (url.contains(".")) {
            return controllerMappingMap.get("resource");
        }
        return controllerMappingMap.get(url);
    }
}
