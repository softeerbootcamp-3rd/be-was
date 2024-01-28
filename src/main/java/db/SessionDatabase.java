package db;

import model.Session;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionDatabase {
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    public static String addSession(Session session) {
        sessions.put(session.getSessionId(), session);
        return session.getSessionId();
    }

    public static Session findSessionById(String sessionId) {
        if (sessionId == null) return null;
        return sessions.get(sessionId);
    }

    public static void deleteSessionById(String sessionId) {
        sessions.remove(sessionId);
    }
}
