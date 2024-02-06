package controller;

import http.HttpStatus;
import http.request.HttpRequest;
import http.response.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;

import java.util.Map;

/**
 * 스프링의 controller 역할
 * controller - service 연결
 */
public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private static final UserService userService = new UserService();

    public static HttpResponse signup(HttpRequest request) {
        Map<String, String> body = request.getParams();
        User user = new User(body.get("userId"), body.get("password"), body.get("name"), body.get("email"));

        try {
            userService.createUser(user);
            // 회원가입에 성공할 경우 index.html로 리다이렉션
            Map<String, String> header = Map.of("Location", "/index.html");
            return HttpResponse.of(HttpStatus.REDIRECT, header);
        } catch (Exception e) {
            logger.error(e.getMessage());
            // 회원가입에 실패할 경우 (중복 아이디) 회원가입 실패 페이지 반환
            Map<String, String> header = Map.of("Location", "/user/signup_failed.html");
            return HttpResponse.of(HttpStatus.REDIRECT, header);
        }
    }

    public static HttpResponse login(HttpRequest request) {
        Map<String, String> body = request.getParams();
        try {
            String sid = userService.join(body.get("userId"), body.get("password"));
            // 로그인에 성공할 경우 index.html로 리다이렉션 되며 쿠키 값에 sid값 추가
            Map<String, String> header = Map.of("Location", "/index.html", "Set-Cookie", "sid=" + sid + "; Max-Age=3600; HttpOnly; Path=/");
            return HttpResponse.of(HttpStatus.REDIRECT, header);
        } catch (Exception e) {
            logger.error(e.getMessage());
            // 로그인에 실패할 경우 /user/login_failed.html로 리다이렉션
            Map<String, String> header = Map.of("Location", "/user/login_failed.html");
            return HttpResponse.of(HttpStatus.REDIRECT, header);
        }
    }

    public static HttpResponse logout(HttpRequest request) {
        // 세션 아이디 값을 생성하여 바로 파기되도록 Max-Age=0으로 함
        Map<String, String> header = Map.of("Location", "/index.html", "Set-Cookie", "sid=1; Max-Age=0; HttpOnly; Path=/");
        return HttpResponse.of(HttpStatus.REDIRECT, header);
    }
}
