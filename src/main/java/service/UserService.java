package service;

import common.exception.LoginFailException;
import db.Database;
import common.exception.DuplicateUserIdException;
import dto.LoginRequest;
import model.User;

import java.util.NoSuchElementException;

public class UserService {

    public String create(User user) {
        String userId = user.getUserId();
        if (Database.findUserById(userId) != null) {
            throw new DuplicateUserIdException();
        }
        Database.addUser(user);
        return user.getUserId();
    }

    public String login(LoginRequest loginRequest) {
        String userId = loginRequest.getUserId();
        User user = Database.findUserById(userId);

        // 입력한 사용자 아이디가 회원 db에 없는 경우
        if (user == null) {
            throw new NoSuchElementException();
        }
        // 비밀번호가 틀린 경우
        if (!user.getPassword().equals(loginRequest.getPassword())) {
            throw new LoginFailException();
        }

        return user.getUserId();
    }
}
