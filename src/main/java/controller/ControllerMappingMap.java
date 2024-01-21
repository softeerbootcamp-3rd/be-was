package controller;

import java.util.HashMap;
import java.util.Map;

public class ControllerMappingMap {
    private static final Map<String, Controller> controllerMappingMap = new HashMap<>();

    static {
        controllerMappingMap.put("html", new HtmlController());
        controllerMappingMap.put("/user/create", new MemberJoinController());
    }

    public static Controller getController(String url) {
        if (url.endsWith(".html")) {
            return controllerMappingMap.get("html");
        }
        if (url.contains("?")) {
            return controllerMappingMap.get(url.substring(0, url.indexOf("?")));
        }
        return null;
    }



}
