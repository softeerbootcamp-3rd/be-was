package service;

import model.HttpRequest;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class SessionManager {
    private static final Duration SESSION_EXPIRATION = Duration.ofMinutes(30);
    private static Map<String, Session> sessionStore = new ConcurrentHashMap<>();

    private static class Session {
        String userId;
        Instant expirationTime;
        Session(String userId, Instant expirationTime) {
            this.userId = userId;
            this.expirationTime = expirationTime;
        }
    }
    public static String createSession(String userId) {
        String sessionId = UUID.randomUUID().toString();
        Instant expirationTime = Instant.now().plus(SESSION_EXPIRATION);
        sessionStore.put(sessionId, new Session(userId, expirationTime));

        return sessionId;
    }

    public static boolean existingSession(HttpRequest httpRequest, String userId) {
        Session session = sessionStore.get(httpRequest.getSessionId());
        return session != null && Objects.equals(session.userId, userId);
    }
}
