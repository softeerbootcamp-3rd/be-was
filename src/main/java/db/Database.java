package db;

import com.google.common.collect.Maps;

import model.User;

import java.util.*;

public class Database {


    private static Map<String, User> users = Collections.synchronizedMap(new HashMap<>());

    public static void addUser(User user) {
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static List<User> findAll() {
        return new ArrayList<>(users.values());
    }


}
