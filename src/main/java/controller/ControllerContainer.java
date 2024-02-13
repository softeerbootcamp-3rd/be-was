package controller;

import java.util.HashMap;
import java.util.Map;

public class ControllerContainer {

    private static ControllerContainer container = new ControllerContainer();
    private final Map<String, Controller> controllerMap; //controller list

    private ControllerContainer() {
        controllerMap = new HashMap<>();
        controllerMap.put("/user", new UserController());
        controllerMap.put("/post", new PostController());
        controllerMap.put("/index", new IndexController());
    }

    public static ControllerContainer getInstance() {
        return container;
    }

    public Controller get(String path) {
        return controllerMap.get(path);
    }
}
