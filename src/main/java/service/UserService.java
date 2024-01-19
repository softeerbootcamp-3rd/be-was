package service;

import db.Database;
import dto.request.UserDto;
import model.User;

public class UserService {
    public static void signUp(UserDto userDto) {
        User user = new User(userDto.getUserId(), userDto.getPassword(), userDto.getName(), userDto.getEmail());
        if(Database.findUserById(user.getUserId()) != null){
            throw new IllegalArgumentException("이미 존재하는 아이디입니다.");
        }

        Database.addUser(user);
    }
}
