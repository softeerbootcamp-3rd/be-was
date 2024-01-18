package service;

import db.Database;
import model.User;
import java.util.Map;

public class UserService {

    public String create(Map<String, String> userInfo) {
        User user = new User(userInfo.get("userId"), userInfo.get("password"), userInfo.get("name"), userInfo.get("email"));
        Database.addUser(user);
        return user.getUserId();
    }
}
