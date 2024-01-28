package service;

import db.Database;
import db.Session;
import model.User;
import request.user.LoginRequest;

import java.util.List;
import java.util.UUID;

import static db.Database.*;

public class UserService {
    private volatile static UserService instance;

    private UserService() {
    }

    public static UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    public List<User> findAll() {
        return Database.findAll();
    }

    // 회원가입 요청을 처리하는 메소드
    public void join(User user) {
        addUser(user);
    }

    // 로그인 요청을 처리하는 메소드
    public boolean login(LoginRequest loginRequest) {

        User user = findUserById(loginRequest.getUserId());

        // 데이터베이스에서 아이디에 해당하는 회원정보가 없거나 비밀번호가 일치하지 않으면 false 반환
        if (user == null || !user.getPassword().equals(loginRequest.getPassword())) {
            return false;
        }

        String uuid = UUID.randomUUID().toString();
        Session.addSession(uuid, loginRequest.getUserId());

        return true;
    }

    public void logout(String sessionId) {
        Session.removeSession(sessionId);
    }
}
