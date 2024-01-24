package controller;

import java.util.HashMap;
import java.util.Map;

public class ControllerMappingMap {
    private static final Map<String, Controller> controllerMappingMap = new HashMap<>();

    static {
        controllerMappingMap.put("resource", new ResourceController());
        controllerMappingMap.put("POST /user/create", new MemberJoinController());
        controllerMappingMap.put("POST /user/login", new MemberLoginController());
        controllerMappingMap.put("POST /user/logout", new MemberLogoutController());
    }

    public static Controller getController(String method, String url) {
        if (url.contains(".")) {
            return controllerMappingMap.get("resource");
        }
        return controllerMappingMap.get(method + " " + url);
    }
}
