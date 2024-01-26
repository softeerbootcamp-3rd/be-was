package db;

import com.google.common.collect.Maps;
import model.Qna;
import model.User;

import java.util.Collection;
import java.util.Map;

public class QnaDatabase {
    private static Map<Long, Qna> qnas = Maps.newHashMap();

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
