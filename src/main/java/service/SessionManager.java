package service;

import model.HttpResponse;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final String COOKIE_NAME = "sid";
    private static Map<String, String> sessionStore = new ConcurrentHashMap<>();
    public static void createSession(String userId, HttpResponse response) {
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, userId);
        response.addCookie(COOKIE_NAME, sessionId);
    }
}
