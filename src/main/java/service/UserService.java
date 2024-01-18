package service;

import db.Database;
import model.User;
import model.UserInfo;

public class UserService {

    public static User create(UserInfo info) {
        User user = new User(info);
        if(user.verifyUser()) {
            Database.addUser(user);
            return user;
        }
        else return null;
    }
}
