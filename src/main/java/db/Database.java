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
		return users.get(userId); //hashmap 때문에 key가 null이면 null 반환
    }

    public static Collection<User> findAll() {
        return users.values();
    }

	public static void removeAll() {
		users.clear();
	}
}
