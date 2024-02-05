package model;

import model.User;

import java.util.Map;

public class UserFactory {
    private UserFactory() {
    }

    public static User createUserFrom(Map<String, String> data){
        String userId = data.get("userId");
        String password = data.get("password");
        String name = data.get("name");
        String email = data.get("email");
        return new User(userId, password, name, email);
    }
}
