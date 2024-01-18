package webApplicationServer.service;

import config.AppConfig;
import db.Database;
import dto.UserSignUpDto;
import exception.BadRequestException;
import model.User;

import java.util.Optional;

public class UserServiceImpl implements UserService {
    @Override
    public void signUp(UserSignUpDto userSignUpDto) {
        Optional<User> userById = Database.findUserById(userSignUpDto.getId());
        if(userById.isPresent()){
            throw new BadRequestException("ID already exists");
        }
        else{
            User user = new  User(userSignUpDto.getId(), userSignUpDto.getName(), userSignUpDto.getName(), userSignUpDto.getName());
            Database.addUser(user);
        }
    }

    public static class UserServiceHolder {
        private static final UserService INSTANCE = new UserServiceImpl();
    }

    public static UserService getInstance() {
        return UserServiceHolder.INSTANCE;
    }
}