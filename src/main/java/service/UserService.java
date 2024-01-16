package service;

import db.Database;
import dto.UserDto;
import model.User;

public class UserService {

    public void saveUser(UserDto userDto) {
        User user = new User(userDto.getUserId(), userDto.getPassword(), userDto.getName(),
                userDto.getEmail());
        Database.addUser(user);
    }
}
