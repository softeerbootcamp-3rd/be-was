package db;

import com.google.common.collect.Maps;

import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;

public class Database {
    private static final Logger logger = LoggerFactory.getLogger(Database.class);

    private static Map<String, User> users = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
        logger.debug("데이터베이스에 유저 추가됨: {}", user);
    }
    public static Optional<User> findUserById(String userId) {
        return Optional.ofNullable(users.get(userId));
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
