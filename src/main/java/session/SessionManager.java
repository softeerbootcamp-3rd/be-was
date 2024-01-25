package session;

import common.util.Util;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class SessionManager {

    private static final Map<String, User> session = new HashMap<>();

    public static String createSessionId(User user) {
        String sessionId = Util.getRandomString();
        session.put(sessionId, user);
        return sessionId;
    }

    public static User getUser(String sessionId) {
        return session.get(sessionId);
    }
}
