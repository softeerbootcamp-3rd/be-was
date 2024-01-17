package service;

import db.Database;
import dto.UserDto;
import model.User;

import java.util.Map;

public class UserService {

    public void createUser(UserDto userDto) {
        User user = new User(userDto.getUserId(),
                userDto.getPassword(),
                userDto.getName(),
                userDto.getEmail());
        Database.addUser(user);
    }
}
