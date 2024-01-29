package repository;

import com.google.common.collect.Maps;
import config.AppConfig;
import model.User;

import java.util.Collection;
import java.util.Map;

public class UserRepository {
    private static Map<String, User> users = Maps.newHashMap();
    static {
        AppConfig.userRepository().
                addUser(new User("test", "test", "test", "test@test"));
    }

    public void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public User findUserById(String userId) {
        return users.get(userId);
    }

    public Collection<User> findAll() {
        return users.values();
    }

    public void clear() {
        users.clear();
    }

}
