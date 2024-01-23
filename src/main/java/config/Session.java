package config;


import java.util.HashMap;
import java.util.UUID;

public class Session {
    private final HashMap<UUID, String> sessions;

    public Session(){
        this.sessions = new HashMap<>();
    }

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
