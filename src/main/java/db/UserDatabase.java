package db;

import com.google.common.collect.Maps;
import model.User;

import java.util.Collection;
import java.util.Map;

public class UserDatabase {
    private static final Map<String, User> users = Maps.newHashMap();

    public static void add(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findById(String userId) {
        return users.get(userId);
    }

    public static User findByIdOrEmpty(String userId) {
        User user = users.get(userId);
        if (user == null)
            return new User("", "", "(알 수 없음)", "");
        return user;
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
