package db;

import com.google.common.collect.Maps;

import model.User;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class Database {
    private static Map<String, User> users = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }

    public static String findUserNameById(String userId) {
        return users.get(userId).getName();
    }

    public static boolean isUserIdExist(String userId) {
        return users.containsKey(userId);
    }

    public static boolean isEmailExist(String email) {
        return users.values().stream()
                .anyMatch(user -> user.getEmail().equals(email));
    }

}
