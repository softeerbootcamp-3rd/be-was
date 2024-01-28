package session;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    public static final String COOKIE_KEY = "sid";
    public static final Long TTL = 3600 * 1000L;
    private static final ConcurrentHashMap<String, Session> storage = new ConcurrentHashMap<>();

    public static Session createSession() {
        String sessionId = UUID.randomUUID().toString();
        Session session = new Session(sessionId);
        storage.put(sessionId, session);
        return session;
    }

    public static Session distroySession(String sessionId) {
        return storage.remove(sessionId);
    }

    public static Session getSession(String sessionId) {
        return storage.get(sessionId);
    }

    public static void setSession(Session session) {
        storage.put(session.getSessionId(), session);
    }

    public static String toCookieString(Session session) {
        return COOKIE_KEY + "=" + session.getSessionId() + "; HttpOnly; Path=/; Max-Age=3600";
    }
}
