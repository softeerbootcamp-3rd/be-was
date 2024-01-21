package service;

import db.Database;
import java.util.Map;
import model.User;

public class UserService {

    public void saveUser(Map<String, String> params) throws NullPointerException, IllegalArgumentException {
        try {
            User user = new User(params.get("userId"), params.get("password"), params.get("name"),
                    params.get("email"));
            Database.addUser(user);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException();
        }
    }
}
