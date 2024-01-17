package service;

import db.Database;
import model.User;

public class UserService {

    public String create(User user) {
        Database.addUser(user);
        return user.getUserId();
    }
}
