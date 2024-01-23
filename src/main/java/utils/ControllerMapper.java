package utils;

import controller.UserController;
import http.response.HttpResponse;
import http.request.HttpRequest;
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

public class ControllerMapper {
    private static final Logger logger = LoggerFactory.getLogger(ControllerMapper.class);
    // Key : 메소드 url-path, Value : controller
    public static final Map<String, Function<HttpRequest, HttpResponse>> CONTROLLER = new HashMap<>();

    static {
        CONTROLLER.put("POST /user/create", UserController::signup);
    }

    public static Function<HttpRequest, HttpResponse> getController(HttpRequest request) {
        String path = request.getRequestLine().getUri();
        if (path.contains("?")) {
            path = path.split("\\?", 2)[0];
        }
        logger.info("path = " + request.getRequestLine().getMethod() + path);
        return CONTROLLER.get(request.getRequestLine().getMethod() + " " + path);
    }

}
