package db;

import model.Session;
import model.User;

import java.util.HashMap;
import java.util.Map;

public class SessionStorage {
    public static final long SESSION_TIME = 3600L;
    public final static Map<String, Session> storage = new HashMap<>();

    public static boolean containsSessionId(String sessionId) {return storage.containsKey(sessionId);}
    public static Session findBySessionId(String sessionId) {
        return storage.get(sessionId);
    }
    public static void verifySession(String sessionId) {
        if(sessionId == null) return;
        Session session = findBySessionId(sessionId);
        if(session == null) {
            sessionId = null;
            return;
        }
    }
    public static void addSession(Session session) {
        storage.put(session.getSessionId(), session);
    }

    public static void updateStorage() {

    }
}
