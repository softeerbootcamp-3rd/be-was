package util;

import model.User;
import util.http.HttpRequest;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final Map<String, User> sessionMap = new ConcurrentHashMap<>();

    public static String generateSessionId() {
        return UUID.randomUUID().toString();
    }

    public static void addSession(String sessionId, User user) {
        sessionMap.put(sessionId, user);
    }

    public static boolean isLoggedIn(HttpRequest httpRequest) {
        return getLoggedInUser(httpRequest) != null;
    }

    public static User getLoggedInUser(HttpRequest httpRequest) {
        String sessionId = httpRequest.getCookieMap().get("SID");
        return getUserBySessionId(sessionId);
    }

    public static User getUserBySessionId(String sessionId) {
        if (sessionId == null) return null;
        return sessionMap.get(sessionId);
    }

    public static void removeSession(HttpRequest httpRequest) {
        String sessionId = httpRequest.getCookieMap().get("SID");
        sessionMap.remove(sessionId);
    }
}

