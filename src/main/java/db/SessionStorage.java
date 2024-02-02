package db;

import model.Session;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

public class SessionStorage {
    public static final long SESSION_TIME = 3600L;
    public final static Map<String, Session> storage = new HashMap<>();
    public final static PriorityQueue<Session> storagePriorityQueue = new PriorityQueue<>();

    public static Session findBySessionId(String sessionId) {
        return storage.get(sessionId);
    }
    public static String verifySession(String sessionId) {
        if(sessionId == null || findBySessionId(sessionId) == null)
            return null;
        return sessionId;
    }
    public static void addSession(Session session) {
        storage.put(session.getSessionId(), session);
        storagePriorityQueue.add(session);
    }
    public static void removeSession(String sessionId) {storage.remove(sessionId);}

    public static void updateStorage() {
        while(!storagePriorityQueue.isEmpty()) {
            Session session = storagePriorityQueue.peek();
            if(session.getExpiredTime().isAfter(LocalDateTime.now())) break;
            removeSession(session.getSessionId());
            storagePriorityQueue.remove();
        }
    }
}
