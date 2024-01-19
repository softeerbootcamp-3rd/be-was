package db;

import com.google.common.collect.Maps;

import model.User;

import java.util.*;

public class Database {


    private static Map<String, User> users = Collections.synchronizedMap(new HashMap<>());

    public static void addUser(User user) {
        System.out.println("Adding user: " + user);
        users.put(user.getUserId(), user);
        System.out.println("User added: " + user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {

        System.out.println("Retrieving all users");
        return users.values();

    }


}
