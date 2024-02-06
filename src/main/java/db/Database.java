//test
package db;

import model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class Database {
    private static ConcurrentHashMap<String, User> users = new ConcurrentHashMap<>();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }
}
