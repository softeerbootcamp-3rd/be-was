package service;

import db.Database;
import common.exception.DuplicateUserIdException;
import model.User;

public class UserService {

    public String create(User user) {
        String userId = user.getUserId();
        if (Database.findUserById(userId) != null) {
            throw new DuplicateUserIdException();
        }
        Database.addUser(user);
        return user.getUserId();
    }
}
