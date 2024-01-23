package service;

import db.Database;
import dto.Login;
import dto.UserDTO;
import model.Session;
import model.User;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public class UserService {
    public String registerUser(UserDTO userInfo){
        //TODO: 이미 가입한 사람인지 확인

        //Database에 새로운 유저 저장
        User newbie = new User(userInfo.getUserId(), userInfo.getPassword(), userInfo.getName(), userInfo.getEmail());
        Database.addUser(newbie);
        return userInfo.getUserId();
    }

    public boolean isValidUser(Login loginInfo){
        User target = Database.findUserById(loginInfo.getUserId());
        return target != null && target.getPassword().equals(loginInfo.getPassword());
    }

    public Session login(Login loginInfo){
        //Session Id 만들기
        String sessionId = UUID.randomUUID().toString();
        Session newSession = new Session(sessionId, loginInfo.getUserId(), LocalDateTime.now());
        Database.addSession(newSession);
        return newSession;
    }
}
