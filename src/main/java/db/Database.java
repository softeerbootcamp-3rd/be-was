package db;

import com.google.common.collect.Maps;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import webserver.RequestHandler;

import java.util.Collection;
import java.util.Map;

public class Database {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private static Map<String, User> users = Maps.newHashMap();

    public static void addUser(User user) {
        validate(user);
        users.put(user.getUserId(), user);
        logger.debug(user.toString());
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    private static void validate(User user) {
        if (users.containsKey(user.getUserId())) {
            throw new IllegalArgumentException("User ID already exists: " + user.getUserId());
        }
    }
}
