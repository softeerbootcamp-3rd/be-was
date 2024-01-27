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

    public static Session findValidSessionByUserId(String userId) {
        for (Session session: sessions.values()) {
            if (session.getUserId().equals(userId) && session.getIsValid()) {
                return session;
            }
        }
        return null;
    }

    public static Session findValidSessionById(String sessionId) {
        for (Session session: sessions.values()) {
            if (session.getSessionId().equals(sessionId) && session.getIsValid()) {
                return session;
            }
        }
        return null;
    }

    public static void invaldateSession(Session session) {
        session.setIsValid(false);
        sessions.put(session.getSessionId(), session);
    }
}
