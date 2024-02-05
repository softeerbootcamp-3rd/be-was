package controller;

import annotation.GetMapping;
import annotation.PostMapping;
import db.Database;
import db.SessionManager;
import http.HttpResponse;
import model.User;
import model.UserFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Optional;

/**
 * "redirect:" 를 반환하는 경우 index.html로 이동한다.
 */
public class Controller {
    private static final Logger logger = LoggerFactory.getLogger(Controller.class);
    private final String USER_ID = "userId";
    private final String PASSWORD = "password";
    private final String SID = "sid";

    //회원가입
    @PostMapping(value = "/user/create")
    public HttpResponse createUser(Map<String, String> body) {
        User user = UserFactory.createUserFrom(body);
        Database.addUser(user);
        logger.debug("user 생성 : {}", Database.findUserById(body.get(USER_ID)));

        return HttpResponse.redirect();
    }

    //로그인
    @PostMapping(value = "/user/login")
    public HttpResponse login(Map<String, String> body) {
        Optional<User> user = Database.findUserById(body.get(USER_ID));
        if (user.isEmpty() || !user.get().checkPassword(body.get(PASSWORD))) {
            logger.error("아이디 혹은 비밀번호가 일치하지 않습니다");
            return HttpResponse.ok("/user/login_failed.html");
        }

        String sid = SessionManager.createUserSession(user.get());
        HttpResponse response = HttpResponse.redirect();
        response.addCookie(SID, sid);
        logger.debug("SESSION COOKIE 생성 :: {}", response);
        return response;
    }

    //로그아웃
    @GetMapping(value = "/user/logout")
    public HttpResponse logout(Map<String, String> query){
        String sid = query.get(SID);
        SessionManager.remove(sid);
        logger.debug("로그아웃 : {}", sid);
        return HttpResponse.redirect();
    }
}
