package service;

import db.Database;
import model.User;

public class UserLoginService {

    public User login(String userId, String password) {

        User findUser = Database.findUserById(userId);

        if (findUser == null) {
            return null;
        }

        if (!findUser.getPassword().equals(password)) {
            return null;
        }

        return findUser;


    }
}
