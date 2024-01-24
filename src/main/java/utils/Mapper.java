package utils;

import controller.UserController;
import http.response.HttpResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.function.Function;


/**
 * 스프링의 @GetMapping or @PostMapping 역할
 * 요청 메소드 + URL의 path로 controller 맵핑해주기
 * 고민 : controller를 서비스 별로 파는게 좋을지 메소드 별로 파는게 좋을지
 * 우선 서비스 별로 파 둠
 */

public enum ControllerMapper {
    SIGNUP("POST /user/create", Validator::validateSignUpInfo, UserController::signup),
    LOGIN("POST /user/login", Validator::validateLoginInfo, UserController::login),
    ;

    private String requestURL;
    private Function<Map<String, String>, Boolean> validator;
    private Function<Map<String, String>, HttpResponse> controller;
    private static final Logger logger = LoggerFactory.getLogger(ControllerMapper.class);

    ControllerMapper(String url, Function<Map<String, String>, Boolean> validator,
                     Function<Map<String, String>, HttpResponse> controller) {
        this.requestURL = url;
        this.validator = validator;
        this.controller = controller;
    }

    public Function<Map<String, String>, Boolean> getValidator() {
        return validator;
    }

    public Function<Map<String, String>, HttpResponse> getController() {
        return controller;
    }
}
