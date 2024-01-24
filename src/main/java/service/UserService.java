package service;

import db.Database;
import http.response.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import http.HttpStatus;
import webserver.RequestHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 서비스 로직 구현
 */
public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    // 회원가입
    public HttpResponse createUser(Map<String, String> params) {
        // userId, password, name, email 값이 잘 들어왔는지 확인
        if (!params.containsKey("userId") || !params.containsKey("password")
                || !params.containsKey("name") || !params.containsKey("email")) {
            return HttpResponse.of(HttpStatus.BAD_REQUEST);
        }

        String userId = params.get("userId"), password = params.get("password");
        String name = params.get("name"), email = params.get("email");

        // userId, password, name, email 빈 값 검사
        if (userId.isEmpty() || password.isEmpty() || name.isEmpty() || email.isEmpty()) {
            return HttpResponse.of(HttpStatus.BAD_REQUEST);
        }

        User user = new User(userId, password, name, email);

        // 유저 아이디 중복 검사
        if (!validateDuplicated(userId)) {
            // 고민사항 : BAD_REQUEST를 반환해야 할지 회원가입 폼으로 REDIRECT 시켜야 할지
            Map<String, String> header = Map.of("Location", "/user/form.html");
            return HttpResponse.of(HttpStatus.REDIRECT, header);
        }

        Database.addUser(user);
        logger.info(Database.findAll().toString());
        Map<String, String> header = Map.of("Location", "/index.html");
        return HttpResponse.of(HttpStatus.REDIRECT, header);
    }

    private boolean validateDuplicated(String userId) {
        return Database.findUserById(userId) == null;
    }

    // TODO: 로그인 기능 구현
    public HttpResponse join(Map<String, String> params) {
        String userId = params.get("userId"), password = params.get("password");
        User user = Database.findUserById(userId);

        // user 정보가 없거나 비밀번호 검증이 실패한 경우 로그인 실패
        if (user == null || !password.equals(user.getPassword())) {
            Map<String, String> header = Map.of("Location", "/user/login_failed.html");
            return HttpResponse.of(HttpStatus.REDIRECT, header);
        }

        // TODO: 세션 아이디 임의로 설정
        String sid = UUID.randomUUID().toString();
        Map<String, String> header = Map.of("Location", "/index.html", "Set-Cookie", "sid=" + sid + "; Path=/");
        return HttpResponse.of(HttpStatus.REDIRECT, header);
    }

}
