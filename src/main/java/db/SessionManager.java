package db;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

public class SessionManager {
    private static final Logger logger = LoggerFactory.getLogger(SessionManager.class);
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
            logger.debug("Key: {}, Value : {}",key , user);
        }
    }

    public static void deleteSession(String session){
        sessions.remove(session);
    }
}
