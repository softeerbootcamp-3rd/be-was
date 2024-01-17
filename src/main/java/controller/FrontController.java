package controller;

import java.util.HashMap;
import java.util.Map;

public class FrontController {
    private static Map<String, UserController> controllerMap = new HashMap<>();

    static {
        controllerMap.put("/user/create", new UserController());
        controllerMap.put("/index.html", new UserController());
        controllerMap.put("/user/form.html", new UserController());
    }

    public static UserController getController(String path) {
        for (String key : controllerMap.keySet()) {
            if (path.startsWith(key)) {
                return controllerMap.get(key);
            }
        }
        return null;
    }
}
