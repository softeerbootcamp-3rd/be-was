package service;

import db.Database;
import model.User;

public class UserService {
    public void createUser(String userId, String password, String name, String email){
        User user = new User.Builder()
                .userId(userId)
                .password(password)
                .name(name)
                .email(email)
                .build();

        Database.addUser(user);
    }
}
