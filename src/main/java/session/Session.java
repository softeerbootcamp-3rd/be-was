package session;

import java.util.*;

public class Session {
    private static Map<String, String> session = new HashMap<>();
    private static Map<String, Long> lastAccessTime = new HashMap<>();
    private static final long INVALIDATE_TIME = 600000; // 1시간

    public static String setAttribute(String userId) {
        String sessionId = makeSessionId();
        session.put(sessionId, userId);
        lastAccessTime.put(sessionId, System.currentTimeMillis());
        return sessionId;
    }

    public static String getAttribute(String sessionId) {
        checkSessionExpiration();
        String userId;
        if ((userId = session.get(sessionId)) != null) {
            lastAccessTime.put(sessionId, System.currentTimeMillis());
        }
        return userId;
    }

    private static void removeSession(String sessionId) {
        session.remove(sessionId);
        lastAccessTime.remove(sessionId);
    }

    private static void checkSessionExpiration() {
        for (String sessionId : session.keySet()) {
            if (lastAccessTime.get(sessionId) + INVALIDATE_TIME < System.currentTimeMillis())
                removeSession(sessionId);
        }
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
