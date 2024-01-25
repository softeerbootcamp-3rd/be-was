package controller;

import java.util.HashMap;
import java.util.Map;

public class ControllerMappingMap {
    private static final Map<String, Controller> controllerMappingMap = new HashMap<>();

    static {
        controllerMappingMap.put("resource", new ResourceController());
        controllerMappingMap.put("/user/create", new MemberJoinController());
        controllerMappingMap.put("/user/login", new MemberLoginController());
        controllerMappingMap.put("/user/logout", new MemberLogoutController());
        controllerMappingMap.put("/user/list.html", new MemberListController());
    }

    public static Controller getController(String method, String url) {
        if (url.contains(".") && !url.equals("/user/list.html")) {
            return controllerMappingMap.get("resource");
        }
        return controllerMappingMap.get(url);
    }
}
