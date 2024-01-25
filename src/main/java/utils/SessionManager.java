package utils;

import com.google.common.collect.Maps;
import model.User;

import java.util.Map;
import java.util.UUID;

public class SessionManager {
    public static Map<String, User> sessions = Maps.newHashMap();

    public static String getSessionId() {
        return UUID.randomUUID().toString();
    }

    public static void addSession(String sid, User user) {
        sessions.put(sid, user);
    }

    public static User findUserBySessionId(String sid) {
        return sessions.get(sid);
    }
}
