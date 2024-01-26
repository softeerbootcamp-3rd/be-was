package service;

import db.Database;
import exception.WebServerException;
import model.User;
import session.SessionManager;

import java.util.Map;

import static constant.ErrorCode.*;


public class UserService {

    // 회원가입 - 회원 id, 이메일 중복 확인
    public static void signUp(Map<String, String> body) {
        User newUser = new User(body);

        if (Database.findUserById(newUser.getUserId()) != null) {
            throw new WebServerException(USER_ID_DUPLICATED);
        } else if (Database.findUserByEmail(newUser.getEmail()) != null) {
            throw new WebServerException(USER_EMAIL_DUPLICATED);
        }

        Database.addUser(newUser);
    }

    public static String login(String userId, String password) {
        User loginUser;

        // 회원가입 하지 않은 유저 OR 비밀번호가 틀린 경우
        if ((loginUser = Database.findUserById(userId)) == null) {
            throw new WebServerException(USER_NOT_FOUND);
        } else if (!loginUser.getPassword().equals(password)) {
            throw new WebServerException(USER_WRONG_PASSWORD);
        }

        // TODO 로그인 후 저장해둘 유저의 정보가 생기면 저장
        // String sessionId = SessionManager.createSession();
        // Session session = SessionManager.getSession(sessionId);
        // session.setAttribute("", User);

        // 새로운 세션 생성후 sessionId 리턴
        return SessionManager.createSession();
    }
}
