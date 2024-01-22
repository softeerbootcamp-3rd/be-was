package service;

import db.Database;
import model.User;

import java.util.Map;

public class UserService {

    public void createUser(Map<String, String> createUserParams) {
        String userId = createUserParams.get("userId");
        String password = createUserParams.get("password");
        String name = createUserParams.get("name");
        String email = createUserParams.get("email");

        if (userId != null && password != null && name != null && email != null) {
            Database.addUser(new User(userId, password, name, email));
        } else {
            throw new IllegalArgumentException("Invalid Parameters");
        }
    }
}
