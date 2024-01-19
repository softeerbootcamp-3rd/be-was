package db;

import com.google.common.collect.Maps;

import model.User;

import java.util.Collection;
import java.util.Map;

public class Database {
    private static Database instance = new Database();

    private static Map<String, User> users = Maps.newHashMap();

    public static Database getInstance() {
        return instance;
    }

    public static void addUser(User user) {
        System.out.println(user.getUserId());
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
