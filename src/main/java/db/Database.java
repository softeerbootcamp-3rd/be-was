package db;

import com.google.common.collect.Maps;

import model.Session;
import model.User;

import java.util.Collection;
import java.util.Map;

public class Database {
    private static Map<String, User> users = Maps.newHashMap();
    private static Map<String, Session> sessions = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    public static void addSession(Session session){
        sessions.put(session.getSessionId(), session);
    }

    public static Session findSessionById(String sessionId){
        return sessions.get(sessionId);
    }
}
