package service;

import db.Database;
import model.User;

import java.util.List;
import java.util.UUID;

public class UserService {
    private static final UserService instance = new UserService();

    private UserService() {}

    public static UserService getInstance() {
        return instance;
    }

    public User registerUser(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);
        Database.addUser(user);
        return user;
    }

    public String loginUser(String userId, String password) {
        User user = Database.findUserById(userId);
        if (user != null && user.getPassword().equals(password)) {
            return UUID.randomUUID().toString(); // 세션 ID 생성
        }
        return null;
    }

    public User getUserById(String userId) {
        return Database.findUserById(userId);
    }
    public List<User> getAllUsers() {
        return Database.getAllUsers(); // Database 클래스에서 모든 사용자 정보를 가져오는 메서드
    }
}
