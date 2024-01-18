package service;

import db.Database;
import dto.UserCreateRequestDto;
import model.User;

public class UserService {

    public void create(UserCreateRequestDto userRequestDto){
        User user = new User(userRequestDto.getUserId(), userRequestDto.getPassword(),
                userRequestDto.getName(), userRequestDto.getEmail());

        Database.addUser(user);
    }
}