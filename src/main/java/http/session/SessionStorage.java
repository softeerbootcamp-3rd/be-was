package http.session;

import model.User;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionStorage {
    private static final SessionStorage instance = new SessionStorage();
    private Map<String, User> sessions = new ConcurrentHashMap<>();

    private SessionStorage() {}

    public static SessionStorage getInstance() {
        return instance;
    }

    public void addSession(String sessionId, User user) {
        sessions.put(sessionId, user);
    }

    // 세션을 제거하는 메서드
    public void removeSession(String sessionId) {
        sessions.remove(sessionId);
    }

    public User getUserBySessionId(String sessionId) {
        return sessions.get(sessionId);
    }
}
