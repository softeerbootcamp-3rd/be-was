package util;

import db.SessionDatabase;
import db.UserDatabase;
import model.Session;
import model.User;

import java.time.LocalDateTime;
import java.util.Map;

public class SessionUtil {
    // HTTP Request의 쿠키 값을 통해 User 정보 반환
    public static User getUserByCookie(Map<String, String> headers) {
        // 헤더가 존재하지 않는 경우 null 반환
        if (headers == null) return null;
        // 헤더의 쿠키 값 파싱
        Map<String, String> cookies = WebUtil.parseCookie(headers.get("Cookie"));
        String sessionId = cookies.get("sid");
        // 세션 유효성 검사 후 User 정보 반환
        return getUserByValidSessionId(sessionId);
    }

    // 유효한 세션 ID에 해당하는 유저가 있으면 유저 정보 반환, 없으면 null 반환
    private static User getUserByValidSessionId(String sessionId) {
        Session session = getValidSession(sessionId);
        if (session == null) {
            return null;
        }
        return UserDatabase.findUserById(session.getUserId());
    }

    // sessionId에 해당하는 세션이 유효한 지 확인 후 반환; 유효하지 않으면 isValid 필드 업데이트 후 null 반환
    private static Session getValidSession(String sessionId) {
        Session session = SessionDatabase.findValidSessionById(sessionId);
        if (session == null) return null;

        if (!isValidSession(session)) {
            SessionDatabase.invaldateSession(session);
            return null;
        }
        return session;
    }

    // Session의 expireDate를 현재 시간과 비교해서 유효성 여부 리턴
    private static boolean isValidSession(Session session) {
        LocalDateTime now = LocalDateTime.now();
        return session.getExpireDate().isAfter(now);
    }
}
