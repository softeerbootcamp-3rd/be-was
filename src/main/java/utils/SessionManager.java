package utils;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class SessionManager {

    private static Map<String, String> sessionIds = Maps.newConcurrentMap();

    public static void addSessionId(String sessionId, String userId) {
        sessionIds.put(sessionId, userId);
    }

    public static void deleteSessionId(String sessionId) {
        sessionIds.remove(sessionId);
    }

    public static String createSessionId() {
        return UUID.randomUUID().toString();
    }

    public static Collection<String> findAllSessionIds() {
        return sessionIds.keySet();
    }

    public static String getUserId(String sessionId) {
        return sessionIds.get(sessionId);
    }

    public static boolean isLoggedInUser(String sessionId){
        String userId = sessionIds.get(sessionId);
        return userId != null;
    }

}
