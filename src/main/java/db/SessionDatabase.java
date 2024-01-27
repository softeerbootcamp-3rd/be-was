package db;

import com.google.common.collect.Maps;
import model.Session;

import java.util.Map;

public class SessionDatabase {
    private static final Map<String, Session> sessions = Maps.newHashMap();

    public static String addSession(Session session) {
        sessions.put(session.getSessionId(), session);
        return session.getSessionId();
    }

    public static Session findSessionById(String sessionId) {
        return sessions.get(sessionId);
    }

    public static void deleteSessionById(String sessionId) {
        sessions.remove(sessionId);
    }
}
