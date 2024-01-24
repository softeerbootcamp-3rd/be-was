package db;

import com.google.common.collect.Maps;
import model.User;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;

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
        sessions.put(user.getUserId(), user);
        return session;
    }
}
