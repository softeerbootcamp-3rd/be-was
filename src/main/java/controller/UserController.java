package controller;

import http.HttpStatus;
import http.response.HttpResponse;
import http.request.HttpRequest;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import utils.Parser;

import java.util.Map;

/**
 * 스프링의 controller 역할
 * controller - service 연결
 */
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final UserService userService = new UserService();

    public static HttpResponse signup(Map<String, String> body) {
        User user = new User(body.get("userId"), body.get("password"), body.get("name"), body.get("email"));

        try {
            userService.createUser(user);
            // 회원가입에 성공할 경우 index.html로 리다이렉션
            Map<String, String> header = Map.of("Location", "/index.html");
            return HttpResponse.of(HttpStatus.REDIRECT, header);
        } catch (Exception e) {
            logger.error(e.getMessage());
            // 회원가입에 실패할 경우 (중복 아이디) CONFLICT 반환
            return HttpResponse.of(HttpStatus.CONFLICT);
        }
    }

    public static HttpResponse login(Map<String, String> body) {
        try {
            String sid = userService.join(body.get("userId"), body.get("password"));
            // 로그인에 성공할 경우 index.html로 리다이렉션 되며 쿠키 값에 sid값 추가
            Map<String, String> header = Map.of("Location", "/index.html", "Set-Cookie", "sid=" + sid + "; Max-Age=360; HttpOnly; Path=/");
            return HttpResponse.of(HttpStatus.REDIRECT, header);
        } catch (Exception e) {
            logger.error(e.getMessage());
            // 로그인에 실패할 경우 /user/login_failed.html로 리다이렉션
            Map<String, String> header = Map.of("Location", "/user/login_failed.html");
            return HttpResponse.of(HttpStatus.REDIRECT, header);
        }
    }
}
