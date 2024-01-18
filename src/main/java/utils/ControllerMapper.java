package utils;

import controller.UserController;
import dto.HttpResponseDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.io.IOException;
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
    public static final Map<String, Function<HttpRequest, HttpResponseDto>> CONTROLLER = new HashMap<>();

    static {
        CONTROLLER.put("GET /user/create", request -> {
            try {
                return UserController.signup(request);
            } catch (IOException e) {
                logger.error(e.getMessage());
                // Internal server error : 파일 입출력 관련
                return HttpResponseDto.of(HttpStatus.INTERNAL_SERVER_ERROR, ContentType.PLAIN_TEXT, e.getMessage().getBytes());
            }
        });
    }

    public static Function<HttpRequest, HttpResponseDto> getController(HttpRequest request) {
        String path = request.getUrl();
        if (path.contains("?")) {
            path = path.split("\\?", 2)[0];
        }
        logger.info("path = " + request.getMethod() + path);
        return CONTROLLER.get(request.getMethod() + " " + path);
    }

}
