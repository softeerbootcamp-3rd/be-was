package util;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final int SESSION_TIMEOUT_SECONDS = 30 * 60; // 30분
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();


    private static class Session {
        private String sessionId;
        private long lastAccessedTime;
        private Map<String, Object> attributes;

        public Session(String sessionId) {
            this.sessionId = sessionId;
            this.lastAccessedTime = System.currentTimeMillis();
            this.attributes = new HashMap<>();
        }

        public String getSessionId() {
            return sessionId;
        }

        public long getLastAccessedTime() {
            return lastAccessedTime;
        }

        public Map<String, Object> getAttributes() {
            return attributes;
        }
    }

    // 세션 생성
    public static String createSession() {
        String sessionId = UUID.randomUUID().toString();
        sessions.put(sessionId, new Session(sessionId));
        return sessionId;
    }

    // 세션 유효시간 체크
    public static boolean checkSessionTimeout(String sessionId) {
        long currentTime = System.currentTimeMillis();
        Session session = sessions.get(sessionId);
        if ( (currentTime - session.getLastAccessedTime()) > SESSION_TIMEOUT_SECONDS * 1000 )
            return true;
        return false;
    }

    // 세션에 속성 추가
    public static void setAttribute(String sessionId, String attributeName, Object attributeValue) {
        Session session = sessions.get(sessionId);
        if (session != null) {
            session.getAttributes().put(attributeName, attributeValue);
            session.lastAccessedTime = System.currentTimeMillis();
        }
    }

    // 세션에서 속성 가져오기
    public static Object getAttribute(String sessionId, String attributeName) {
        Session session = sessions.get(sessionId);
        return (session != null) ? session.getAttributes().get(attributeName) : null;
    }

    // 세션 삭제
    public static void deleteSession(String sessionId) {
        sessions.remove(sessionId);
    }

    // 세션 존재하는 지 확인
    public static boolean isSessionExist(String sessionId) {
        return sessions.containsKey(sessionId) ? true : false;
    }

    // 접속 시간 업데이트
    public static void updateLastAccessedTime(String sessionId) {
        Session session = sessions.get(sessionId);
        session.lastAccessedTime = System.currentTimeMillis();
    }

    public static boolean isValidateSession(String sessionId) {
        if (isSessionExist(sessionId) && !checkSessionTimeout(sessionId))
            return true;
        return false;
    }

}



