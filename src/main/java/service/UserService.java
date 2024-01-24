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
    public void createUser(User user) throws Exception {
        // 유저 아이디 중복 검사
        if (!validateDuplicated(user.getUserId())) {
            throw new Exception("중복된 아이디입니다.");
        }

        Database.addUser(user);
        logger.info(Database.findAll().toString());
    }

    private boolean validateDuplicated(String userId) {
        return Database.findUserById(userId) == null;
    }

    public HttpResponse join(Map<String, String> params) {
        String userId = params.get("userId"), password = params.get("password");
        User user = Database.findUserById(userId);

        // user 정보가 없거나 비밀번호 검증이 실패한 경우 로그인 실패
        if (user == null || !password.equals(user.getPassword())) {
            Map<String, String> header = Map.of("Location", "/user/login_failed.html");
            return HttpResponse.of(HttpStatus.REDIRECT, header);
        }

        // 세션 아이디 랜덤 생성 후 DB에 저장
        String sid = UUID.randomUUID().toString();
        Database.addSession(user, sid);
        // response 헤더에 redirection, cookie 설정 추가
        Map<String, String> header = Map.of("Location", "/index.html", "Set-Cookie", "sid=" + sid + "; Path=/");
        return HttpResponse.of(HttpStatus.REDIRECT, header);
    }

}
