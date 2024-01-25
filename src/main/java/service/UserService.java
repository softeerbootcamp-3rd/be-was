package service;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.SessionManager;
import webserver.RequestHandler;

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

    public String join(String userId, String password) throws Exception {
        User user = Database.findUserById(userId);

        // 입력한 아이디에 대한 user 정보가 없거나 비밀번호 검증이 실패한 경우 로그인 실패
        if (user == null || !password.equals(user.getPassword())) {
            throw new Exception("로그인 실패");
        }

        // 세션 아이디 랜덤 생성 후 반환
        // 세션 아이디와 user 정보 저장 - 고민사항 : 서비스 로직에서 세션 정보 저장 or 컨트롤러에서 저장?
        String sid = SessionManager.getSessionId();
        SessionManager.addSession(sid, user);

        return SessionManager.getSessionId();
    }

}
