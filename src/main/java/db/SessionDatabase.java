package db;

import com.google.common.collect.Maps;

import java.util.Collection;
import java.util.Map;
import java.util.UUID;

public class SessionDatabase {

    private static Map<String, String> sessionIds = Maps.newConcurrentMap();

    public static void addSessionId(String userId, String sessionId) {
        sessionIds.put(userId, sessionId);
    }

    public static void deleteSessionId(String userId) {
        sessionIds.remove(userId);
    }

    public static String createSessionId() {
        return UUID.randomUUID().toString();
    }

    public static Collection<String> findAllSessionIds() {
        return sessionIds.values();
    }

}
