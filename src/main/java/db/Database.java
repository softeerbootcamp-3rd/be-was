package db;

import com.google.common.collect.Maps;

import model.User;

import java.util.Collection;
import java.util.Map;

public class Database {
    private static Map<String, User> users = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static User findUserByEmail(String email) {
        User user;

        for (String id : users.keySet()) {
            user = users.get(id);
            if (user.getEmail().equals(email)){
                return user;
            }
        }

        return null;
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
