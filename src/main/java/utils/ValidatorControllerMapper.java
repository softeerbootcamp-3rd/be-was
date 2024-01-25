package utils;

import controller.UserController;
import http.response.HttpResponse;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


/**
 * method + path 에 맞는 validator(유효성 검증 메소드)와 controller 맵핑
 */

public enum PostControllerMapper {
    SIGNUP(Validator::validateSignUpInfo, UserController::signup),
    LOGIN(Validator::validateLoginInfo, UserController::login),
    ;

    private Function<Map<String, String>, Boolean> validator;
    private Function<Map<String, String>, HttpResponse> controller;

    private static Map<String, PostControllerMapper> MAPPER = new HashMap<>();

    static {
        MAPPER.put("/user/create", SIGNUP);
        MAPPER.put("/user/login", LOGIN);
    }

    PostControllerMapper(Function<Map<String, String>, Boolean> validator, Function<Map<String, String>, HttpResponse> controller) {
        this.validator = validator;
        this.controller = controller;
    }

    public Function<Map<String, String>, Boolean> getValidator() {
        return validator;
    }

    public Function<Map<String, String>, HttpResponse> getController() {
        return controller;
    }

    public static PostControllerMapper getValidatorAndController(String requestURL) {
        return MAPPER.getOrDefault(requestURL, null);
    }
}
