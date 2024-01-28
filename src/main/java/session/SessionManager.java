package session;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SessionManager {
    private static Map<String, Session> sessions = new HashMap<>();
    private static Map<String, Long> lastAccessTime = new HashMap<>();
    private static final long INVALIDATE_TIME = 60 * 60 * 1000; // 1시간

    public static String createSession() {
        String sessionId = UUID.randomUUID().toString();
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
}
