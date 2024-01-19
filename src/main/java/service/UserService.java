package service;

import db.Database;
import dto.UserDTO;
import model.User;

public class UserService {
    public String registerUser(UserDTO userInfo){
        //TODO: 이미 가입한 사람인지 확인

        //Database에 새로운 유저 저장
        User newbie = new User(userInfo.getUserId(), userInfo.getPassword(), userInfo.getName(), userInfo.getEmail());
        Database.addUser(newbie);
        return userInfo.getUserId();
    }
}
