package service;

import db.Database;
import dto.request.UserLoginDto;
import dto.request.UserSignUpDto;
import model.User;

public class UserService {
    public static void signUp(UserSignUpDto userSignUpDto) {
        User user = new User(userSignUpDto.getUserId(), userSignUpDto.getPassword(), userSignUpDto.getName(), userSignUpDto.getEmail());
        if(Database.findUserById(user.getUserId()) != null){
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        Database.addUser(user);
    }

    public static String login(UserLoginDto userLoginDto) {
        User findUser = Database.findUserById(userLoginDto.getUserId());

        if (findUser != null && findUser.getPassword().equals(userLoginDto.getPassword())) {
            return "/index.html";
        }

        return "/user/login_failed.html";
    }
}
