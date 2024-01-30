package service;

import model.HttpResponse;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private static final String COOKIE_NAME = "sid";
    private static final Map<String, String> sessionStore = new ConcurrentHashMap<>();

    public static void createSession(String userId, HttpResponse httpResponse) {
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, userId);
        String setCookieHeader = createCookieHeader(sessionId);
        httpResponse.addCookie(setCookieHeader);
    }

    public static boolean existingSession(String userId, String sessionId, HttpResponse httpResponse) {
        String storedUserId = sessionStore.get(sessionId);

        if (storedUserId != null && Objects.equals(storedUserId, userId)) {
            String setCookieHeader = createCookieHeader(sessionId);
            httpResponse.addCookie(setCookieHeader);
            return true;
        }
        return false;
    }

    private static String createCookieHeader(String sessionId) {
        return String.format("%s=%s; Path=/", COOKIE_NAME, sessionId);
    }
}
