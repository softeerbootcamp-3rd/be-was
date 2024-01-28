package db;

import model.User;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    SessionManager(){

    }

    private static final HashMap<String, User> sessions = new HashMap<>();
    private static SecureRandom random = new SecureRandom();
    public static String generateSessionId() {
        return new BigInteger(130, random).toString(32);
    }

    public static String addSession(User user) {
        String session = generateSessionId();
        sessions.put(session, user);
        return session;
    }

    public static User findUserById(String sessionId) {
        return sessions.get(sessionId);
    }

    public static void printAllUsers() {
        for (Map.Entry<String, User> entry : sessions.entrySet()) {
            String key = entry.getKey();
            User user = entry.getValue();
            System.out.println("Key: " + key + ", Value: " + user);
        }
    }
}
