package webserver;

import controller.Controller;
import controller.ResourceController;
import controller.UserController;

import java.util.HashMap;
import java.util.Map;

public class HandlerMapping {
    private static Map<String, Controller> controllers = new HashMap<>();

    static {
        controllers.put("/user/create", new UserController());
    }

    public Controller findController(String url) {
        return controllers.keySet().stream()
                .filter(url::startsWith)
                .findFirst()
                .map(controllers::get)
                .orElse(new ResourceController());
    }
}
