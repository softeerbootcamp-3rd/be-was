package db;

import model.Session;
import model.User;

import java.util.Map;

public class SessionStorage {
    public static final long SESSION_TIME = 3600L;
    public static Map<String, Session> storage;

    public static boolean containsSessionId(String sessionId) {return storage.containsKey(sessionId);}
    public static void addSession(String sessionId, Session session) {storage.put(sessionId, session);}
    public static Session findBySessionId(String sessionId) {
        return storage.get(sessionId);
    }
}
