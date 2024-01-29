package service;

import db.Database;
import model.HttpResponse;
import model.User;

import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {

    private static final String COOKIE_NAME = "sid";
    private static final Map<String, User> sessionStore = new ConcurrentHashMap<>();

    public static void createSession(String userId, HttpResponse httpResponse) {
        String sessionId = UUID.randomUUID().toString();
        sessionStore.put(sessionId, Database.findUserById(userId));
        String setCookieHeader = createCookieHeader(sessionId);
        httpResponse.addCookie(setCookieHeader);
    }

    public static boolean existingSession(String userId, String sessionId, HttpResponse httpResponse) {
        User user = sessionStore.get(sessionId);

        if (user != null && Objects.equals(user.getUserId(), userId)) {
            String setCookieHeader = createCookieHeader(sessionId);
            httpResponse.addCookie(setCookieHeader);
            return true;
        }
        return false;
    }

    public static User findUserBySessionId(String sessionId){
        return sessionStore.get(sessionId);
    }

    public static void expireSession(String sessionId, HttpResponse httpResponse) {
        sessionStore.remove(sessionId);
        String expireCookieHeader = String.format("%s=; Path=/; Max-Age=0", COOKIE_NAME);
        httpResponse.addCookie(expireCookieHeader);
    }

    private static String createCookieHeader(String sessionId) {
        return String.format("%s=%s; Path=/", COOKIE_NAME, sessionId);
    }
}
