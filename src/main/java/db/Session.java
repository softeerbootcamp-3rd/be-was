package db;

import com.google.common.collect.Maps;
import model.User;

import java.util.Map;

public class Session {
    private static Map<String, String> sessions = Maps.newHashMap();

    public static void addSession(String sessionId, String userId) {
        sessions.put(sessionId, userId);
    }

    public static User findUserBySessionId(String seesionId) {
        String userId = sessions.get(seesionId);
        return Database.findUserById(userId);
    }

    public static String getSessionId(String userId) {
        for (String key : sessions.keySet()) {
            if (sessions.get(key).equals(userId)) {
                return key;
            }
        }
        return null;
    }
}
