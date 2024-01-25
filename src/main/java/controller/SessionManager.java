package controller;

import model.User;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private static SessionManager instance = new SessionManager();
    private static final Map<String, User> sessionStore = new ConcurrentHashMap<>();

    private SessionManager() {

    }

    public static SessionManager getInstance() {
        return instance;
    }

    public static void addSession(String sessionId, User user) {
        sessionStore.put(sessionId, user);
    }

    public static User getUserBySessionId(String sessionId) {
        return sessionStore.get(sessionId);
    }
}
