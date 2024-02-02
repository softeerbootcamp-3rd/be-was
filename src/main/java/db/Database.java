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

    public static User findUserByName(String name){
        for(User user :  users.values()){
            if(name.equals(user.getName())){
                return user;
            }
        }
        return null;
    }

    public static List<User> findAll() {
        return new ArrayList<>(users.values());
    }


}
