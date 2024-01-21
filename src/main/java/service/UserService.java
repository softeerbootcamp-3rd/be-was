package service;

import db.Database;
import http.response.HttpResponse;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import http.HttpStatus;
import webserver.RequestHandler;

import java.util.Map;

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
            return HttpResponse.of(HttpStatus.REDIRECT, "/user/form.html");
        }

        Database.addUser(user);
        logger.info(Database.findAll().toString());
        return HttpResponse.of(HttpStatus.REDIRECT, "/index.html");
    }

    private boolean validateDuplicated(String userId) {
        return Database.findUserById(userId) == null;
    }
}
