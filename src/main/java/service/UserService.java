package service;

import db.Database;
import exception.WebServerException;
import model.User;
import session.Session;
import session.SessionManager;
import webserver.HttpResponse;

import java.util.Map;

import static constant.ErrorCode.*;


public class UserService {

    // 회원가입
    public static void signUp(Map<String, String> body) {
        User newUser = new User(body);

        // null 값이 있는 경우 예외 처리
        if (newUser.getUserId() == null | newUser.getPassword() == null |
        newUser.getName() == null | newUser.getEmail() == null)
            throw new WebServerException(NO_ESSENTIAL_INFO);

        // 회원 id, 이메일 중복 확인
        if (Database.findUserById(newUser.getUserId()) != null) {
            throw new WebServerException(USER_ID_DUPLICATED);
        } else if (Database.findUserByEmail(newUser.getEmail()) != null) {
            throw new WebServerException(USER_EMAIL_DUPLICATED);
        }

        Database.addUser(newUser);
    }

    // 로그인
    public static String login(String userId, String password) {
        User loginUser;

        // 회원가입 하지 않은 유저 OR 비밀번호가 틀린 경우
        if ((loginUser = Database.findUserById(userId)) == null ||
                !loginUser.getPassword().equals(password)) {
            return null;
        }

        // 로그인 후 저장해둘 유저의 정보 (아이디, 이름) 저장
         String sessionId = SessionManager.createSession();
         Session session = SessionManager.getSession(sessionId);
         session.setAttribute("userId", loginUser.getUserId());
         session.setAttribute("userName", loginUser.getName());

        // 새로운 세션 생성후 sessionId 리턴
        return sessionId;
    }
}
