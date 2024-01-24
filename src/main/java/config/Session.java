package config;


import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class Session {
    private static final ConcurrentHashMap<UUID, String> sessions = new ConcurrentHashMap<>();


    UUID createSession(String userId){
        if (exitsByValue(userId))
            return null;
        UUID uuid =  UUID.randomUUID();
        sessions.put(uuid, userId);
        return uuid;
    }

    boolean exitsByValue(String userId){
        return sessions.containsValue(userId);
    }

    String getUserId(UUID uuid){
        return sessions.get(uuid);
    }

}
