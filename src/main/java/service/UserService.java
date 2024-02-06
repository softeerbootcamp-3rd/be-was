package service;

import db.Database;
import common.exception.DuplicateUserIdException;
import dto.LoginRequest;
import model.User;

public class UserService {

    public String create(User user) {
        String userId = user.getUserId();
        if (Database.findUserById(userId) != null) {
            throw new DuplicateUserIdException();
        }
        Database.addUser(user);
        return user.getUserId();
    }

    public User login(LoginRequest loginRequest) {
        String userId = loginRequest.getUserId();
        User user = Database.findUserById(userId);

        // 사용자가 존재하지 않거나 비밀번호가 틀린 경우
        if (user == null || !user.getPassword().equals(loginRequest.getPassword())) {
            return null;
        }

        return user;
    }
}
