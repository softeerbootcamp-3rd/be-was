package service;

import model.User;

import java.util.HashMap;

public class UserService {

    public static User create(HashMap<String, String> info) {
        String userId = info.get("userId");
        String password = info.get("password");
        String name = info.get("name");
        String email = info.get("email");
        User user = new User(userId, password, name, email);
        return (User.verifyUser(user).equals("성공")) ? user : null;
    }
}
