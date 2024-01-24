package utils;

import controller.UserController;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


/**
 * 스프링의 @GetMapping or @PostMapping 역할
 * 요청 메소드 + URL의 path로 controller 맵핑해주기
 * 고민 : controller를 서비스 별로 파는게 좋을지 메소드 별로 파는게 좋을지
 * 우선 서비스 별로 파 둠
 */

public enum ValidatorController {
    SIGNUP(Validator::validateSignUpInfo, UserController::signup),
    LOGIN(Validator::validateLoginInfo, UserController::login),
    ;

    private Function<Map<String, String>, Boolean> validator;
    private Function<Map<String, String>, HttpResponse> controller;
    private static final Logger logger = LoggerFactory.getLogger(ValidatorController.class);

    private static Map<String, ValidatorController> MAPPER = new HashMap<>();

    static {
        MAPPER.put("POST /user/create", SIGNUP);
        MAPPER.put("POST /user/login", LOGIN);
    }

    ValidatorController(Function<Map<String, String>, Boolean> validator, Function<Map<String, String>, HttpResponse> controller) {
        this.validator = validator;
        this.controller = controller;
    }

    public Function<Map<String, String>, Boolean> getValidator() {
        return validator;
    }

    public Function<Map<String, String>, HttpResponse> getController() {
        return controller;
    }

    public ValidatorController getValicatorController(String requestURL) {
        return MAPPER.getOrDefault(requestURL, null);
    }
}
