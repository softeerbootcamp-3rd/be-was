package service;

import data.CookieData;
import data.RequestData;
import db.Database;
import db.Session;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import util.RequestParserUtil;

import java.util.Map;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public static void registerUser(RequestData requestData) {
        Map<String, String> userProps = RequestParserUtil.parseUserRegisterQuery(requestData.getBody());

        String userId = userProps.get("userId");
        String password = userProps.get("password");
        String name = userProps.get("name");
        String email = userProps.get("email");

        if (userProps.size() != 4) {
            logger.error("회원가입에 필요한 파라미터의 수가 부족합니다.");
            throw new IllegalArgumentException("입력 파라미터의 개수가 부적절합니다.");
        }

        // 추출한 데이터로 사용자 생성
        User newUser = new User(userId, password, name, email);

        // DB에 저장
        Database.addUser(newUser);

        logger.debug(newUser.toString());
    }

    public static CookieData login(RequestData requestData) {
        logger.debug("login() method");

        Map<String, String> loginData = RequestParserUtil.parseUserRegisterQuery(requestData.getBody());

        String id = loginData.get("userId");
        String password = loginData.get("password");

        User user = Database.findUserById(loginData.get("userId"));

        if(user != null && user.getPassword().equals(password)) {
            logger.debug("사용자 " + user.getUserId() + " 의 로그인이 성공했습니다.");
            String sid = Session.createSession(user.getUserId());
            return new CookieData(sid, 60 * 5);
        } else {
            logger.debug("ID 입력값 " + id + " 으로 비정상적인 접근이 있었습니다.");
            return null;
        }

    }

    public static boolean isLoggedIn(String cookie) {
        logger.debug("isLoggedIn()");

        String[] sid = cookie.split("=");

        logger.debug("Cookie");
        logger.debug("\t" + sid[0] + " : " + sid[1]);

        // 쿠키에 sid가 없는 경우
        if (!sid[0].equals("sid")) {
            logger.debug("Session is not exists!");
            return false;
        }

        // 쿠키의 세션 id가 서버에 없는 경우
        if (Session.getUserIdBySessionId(sid[1]) == null) {
            logger.debug("Session is not exists!");
            return false;
        }

        logger.debug("Session already exists!");
        return true;
    }

    public static CookieData logout(RequestData requestData) {
        logger.debug("logout()");

        String cookie = requestData.getHeaderValue("Cookie");

        if (cookie != null) {
            String[] tokens = cookie.split("=");

            if (tokens.length >= 2) {
                String cookieName = tokens[0];
                String sid = tokens[1];

                Session.removeSession(sid);
                logger.debug("[Database]");
                for (User user: Database.findAll()) {
                    logger.debug(user.toString());
                }
                logger.debug("[Session]");
                for (String sessionId: Session.getAllSessionId()) {
                    logger.debug("sessionId: " + sessionId + ", userId: " + Session.getUserIdBySessionId(sessionId));
                }
                return new CookieData(sid, 0);
            }
        }

        return null;
    }
}
