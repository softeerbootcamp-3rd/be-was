package service;

import db.Database;
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

    public static String login(String id, String password) {
        User findUser = Database.findUserById(id);

        if (findUser == null || !findUser.getPassword().equals(password)) {
            throw new IllegalArgumentException("아이디 또는 비밀번호가 잘못되었습니다.");
        }

        return id;
    }
}
