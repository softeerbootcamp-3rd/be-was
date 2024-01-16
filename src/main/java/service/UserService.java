package service;

import db.Database;
import model.User;


public class UserService {
    public static User signUp(String params) {
        User newUser = new User(params);
        Database.addUser(newUser);

        return newUser;
    }
}
