package webApplicationServer.service;

import config.AppConfig;
import db.Database;
import dto.UserSignUpDto;
import model.User;

public class UserServiceImpl implements UserService {
    @Override
    public void signUp(UserSignUpDto userSignUpDto) {
        User user = new  User(userSignUpDto.getId(), userSignUpDto.getName(), userSignUpDto.getName(), userSignUpDto.getName());
        Database.addUser(user);
    }

    public static class UserServiceHolder {
        private static final UserService INSTANCE = new UserServiceImpl();
    }

    public static UserService getInstance() {
        return UserServiceHolder.INSTANCE;
    }
}