package service;

import db.Database;
import dto.UserDto;
import model.User;

import java.util.Optional;

public class UserService {
    private final Database database = Database.getInstance();
    private static final UserService instance = new UserService();

    public static UserService getInstance(){
        return instance;
    }

    public void signUp(User user) {
        database.addUser(user);
    }
}
