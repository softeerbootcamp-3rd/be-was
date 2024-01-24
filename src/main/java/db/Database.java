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

    public static Collection<User> findAll() {
        return users.values();
    }

    public static boolean isValidLogin(String id, String pw){
        User user = findUserById(id);
        return user != null && user.getPassword().equals(pw);
    }

    public static void printAllUsers() {
        for (Map.Entry<String, User> entry : users.entrySet()) {
            String key = entry.getKey();
            User user = entry.getValue();
            System.out.println("Key: " + key + ", Value: " + user);
        }
    }
}
