package db;

import com.google.common.collect.Maps;

import model.Qna;
import model.User;

import java.util.Collection;
import java.util.Map;

public class Database {
    private static Map<String, User> users = Maps.newHashMap();
    private static Map<String, Qna> qnas = Maps.newHashMap();

    static {
        addUser(new User("test", "test", "test", "test@test"));
    }

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAllUsers() {
        return users.values();
    }

    public static void clearUsers() {
        users.clear();
    }

    public static void addQna(Qna qna) {
        qnas.put(qna.getWriter(), qna);
    }

    public static Qna findQnaByUsername(String username) {
        return qnas.get(username);
    }

    public static Collection<Qna> findAllQnas() {
        return qnas.values();
    }

}
