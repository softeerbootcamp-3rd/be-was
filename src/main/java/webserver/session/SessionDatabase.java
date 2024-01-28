package webserver.session;

import webserver.session.Session;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionDatabase {
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();
    private static final Map<String, String> userSession = new ConcurrentHashMap<>();

    private SessionDatabase(){}

    public static void addSession(Session session){
        sessions.put(session.getId(), session);
        userSession.put(session.getUserId(), session.getId());
    }

    public static Session loadSession(String sessionId){
        return sessions.getOrDefault(sessionId, null);
    }

    public static boolean existsUserSession(String userId){
        return userSession.containsKey(userId);
    }

    public static void deleteSession(String sessionId){
        Session session = sessions.get(sessionId);

        userSession.remove(session.getUserId());
        sessions.remove(sessionId);
    }
}
