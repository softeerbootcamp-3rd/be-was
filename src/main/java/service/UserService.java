package service;

import db.Database;
import exception.DuplicateUserIdException;
import model.User;
import java.util.Map;

public class UserService {

    public String create(Map<String, String> userInfo) {
        String userId = userInfo.get("userId");
        if (Database.findUserById(userId) != null) {
            throw new DuplicateUserIdException();
        }
        User user = new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"), userInfo.get("email"));
        Database.addUser(user);
        return user.getUserId();
    }
}
