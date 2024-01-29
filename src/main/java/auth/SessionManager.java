package auth;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private static SessionManager instance = new SessionManager();
    private static final Map<String, String> sessionStore = new ConcurrentHashMap<>();

    private SessionManager() {

    }

    public static SessionManager getInstance() {
        return instance;
    }

    public static void addSession(String sessionId, String userId) {
        sessionStore.put(sessionId, userId);
    }

    public static String getUserBySessionId(String sessionId) {
        return sessionStore.get(sessionId);
    }

    public static void deleteSession(String sessionId) {
        sessionStore.remove(sessionId);
    }

}
