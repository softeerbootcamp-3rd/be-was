package util;


import model.User;
import webserver.HttpRequest;

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

    public static boolean isLoggedIn(HttpRequest request) {
        return getLoggedInUser(request) != null;
    }

    public static User getLoggedInUser(HttpRequest request) {
        String sid = RequestParser.parseCookie(request.getHeader().get("Cookie")).get("SID");
        return getUserBySessionId(sid);
    }

    public static User getUserBySessionId(String sessionId) {
        if (sessionId == null) return null;
        return sessionMap.get(sessionId);
    }

    public static void removeSession(String sessionId) {
        sessionMap.remove(sessionId);
    }

}