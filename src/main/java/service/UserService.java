package service;

import db.Database;
import model.User;

import java.util.Optional;

public class UserService {
    private final Database database = Database.getInstance();
    private static final UserService instance = new UserService();

    public static UserService getInstance(){
        return instance;
    }

    public void signUp(User user) {
        database.addUser(user);
    }

    public Optional<User> login(String userId, String password) {
        return Optional.ofNullable(database.findUserById(userId))
                .filter(user -> user.getPassword().equals(password));
    }
}
