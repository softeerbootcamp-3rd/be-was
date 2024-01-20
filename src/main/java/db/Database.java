package db;

import com.google.common.collect.Maps;

import model.User;

import java.util.Collection;
import java.util.Map;

public class Database {

    private static final Map<String, User> users = Maps.newHashMap();

    /**
     * 사용자를 데이터베이스에 저장합니다.
     *
     * <p> 이미 등록된 아이디라면 오류를 발생시킵니다.
     *
     * @param user 사용자 객체
     * @throws IllegalArgumentException 이미 등록된 userId인 경우 발생
     */
    public static void addUser(User user) throws IllegalArgumentException {
        if (users.containsKey(user.getUserId())) {
            throw new IllegalArgumentException();
        }
        users.put(user.getUserId(), user);
    }

    public static User findUserById(String userId) {
        return users.get(userId);
    }

    public static Collection<User> findAll() {
        return users.values();
    }
}
