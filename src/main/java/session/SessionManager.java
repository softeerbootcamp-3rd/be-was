package session;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SessionManager {
    private static Map<String, Session> sessions = new HashMap<>();
    private static Map<String, Long> lastAccessTime = new HashMap<>();
    private static final long INVALIDATE_TIME = 10000; // 1시간

    public static String createSession() {
        String sessionId = makeSessionId();
        sessions.put(sessionId, new Session());
        lastAccessTime.put(sessionId, System.currentTimeMillis());
        return sessionId;
    }

    public static Session getSession(String sessionId) {
        checkSessionExpiration(sessionId);

        Session session;
        if ((session = sessions.get(sessionId)) != null) {
            lastAccessTime.put(sessionId, System.currentTimeMillis());
        }
        return session;
    }

    private static void removeSession(String sessionId) {
        sessions.remove(sessionId);
        lastAccessTime.remove(sessionId);
    }

    private static void checkSessionExpiration(String sessionId) {
        if (lastAccessTime.containsKey(sessionId) &&
                lastAccessTime.get(sessionId) + INVALIDATE_TIME < System.currentTimeMillis())
            removeSession(sessionId);
    }

    public static String makeSessionId() {
        int leftLimit = 48; // '0' 아스키 코드
        int rightLimit = 122; // 'z'
        int targetStringLength = 30;
        Random random = new Random();

        return random.ints(leftLimit,rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97)) // 숫자와, 알파벳 만 필터링
                .limit(targetStringLength) // 설정한 길이만큼
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
    }
}
