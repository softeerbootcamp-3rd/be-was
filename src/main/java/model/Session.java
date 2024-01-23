package model;

import java.util.HashMap;

public class Session {
    private static HashMap<String, User> sessionStorage = new HashMap<>();

    public static void addSession(String sessionId, User user) {
        sessionStorage.put(sessionId, user);
    }

    public static User findBySessionId(String sessionId) {
        return sessionStorage.get(sessionId);
    }
}
