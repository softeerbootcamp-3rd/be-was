package util;

import controller.Controller;
import controller.DefaultController;
import controller.UserController;
import dto.HttpRequestDto;
import service.UserService;

import java.util.HashMap;
import java.util.Map;

public class ControllerMapper {
    private static final Map<String, Controller> CONTROLLER_MAP = new HashMap<>();

    static {
        CONTROLLER_MAP.put("user", new UserController(new UserService()));
        CONTROLLER_MAP.put("default", new DefaultController());
    }

    public static Controller mappingController(HttpRequestDto request) {
        String uri = request.getUri();
        Controller controller;
        if (uri.startsWith("/user")) {
            return CONTROLLER_MAP.get("user");
        }
        return CONTROLLER_MAP.get("default");
    }
}
