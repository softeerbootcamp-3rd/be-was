package webserver;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import model.User;

public class Session {

    private static final Map<String, User> session = new ConcurrentHashMap<>();

    public static String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    public static void addSession(String sessionId, User user) {
        session.put(sessionId, user);
    }

    public static User getUserBySessionId(String sessionId) {
        return session.get(sessionId);
    }
}
