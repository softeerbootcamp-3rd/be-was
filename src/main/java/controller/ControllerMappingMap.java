package controller;

import java.util.HashMap;
import java.util.Map;

public class ControllerMappingMap {
    private static final Map<String, Controller> controllerMappingMap = new HashMap<>();

    static {
        controllerMappingMap.put("html", new HtmlController());
    }

    public static Controller getController(String url) {
        if (url.endsWith(".html")) {
            return controllerMappingMap.get("html");
        }
        return null;
    }

}
