package util;

import controller.*;
import dto.HttpRequestDto;
import service.QnaService;
import service.UserService;

import java.util.HashMap;
import java.util.Map;

public class ControllerMapper {
    private static final Map<String, Controller> CONTROLLER_MAP = new HashMap<>();

    static {
        CONTROLLER_MAP.put("user", new UserController(new UserService()));
        CONTROLLER_MAP.put("default", new DefaultController());
        CONTROLLER_MAP.put("qna", new QnaController(new QnaService()));
        CONTROLLER_MAP.put("download", new FileController());
    }

    public static Controller mappingController(HttpRequestDto request) {
        String uri = request.getUri();
        Controller controller;
        if (uri.startsWith("/user")) {
            return CONTROLLER_MAP.get("user");
        }
        if (uri.startsWith("/qna")) {
            return CONTROLLER_MAP.get("qna");
        }
        if (uri.startsWith("/download")) {
            return CONTROLLER_MAP.get("download");
        }
        return CONTROLLER_MAP.get("default");
    }
}
