package service;

import db.Database;
import model.User;

public class UserService {
    public String create(String userId, String password, String name, String email) {
        User user = new User(userId, password, name, email);
        Database.addUser(user);
        return user.getUserId();
    }
}