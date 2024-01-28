package service;

import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class UserService {
    public static UserService getInstance() {
        return UserService.LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final UserService INSTANCE = new UserService();
    }

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public boolean saveUser(String userId, String password, String name, String email) {
        if (Database.findUserById(userId) != null) {
            return false;
        }

        try {
            User newUser = new User(userId, password, name, email);
            Database.addUser(newUser);
            logger.debug("user saved: {}", newUser);
            logger.debug("DB Users: {}", Database.findAll());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public User getUser(String userId, String password) {
        if (Database.findUserById(userId) == null)
            return null;

        User user = Database.findUserById(userId);
        if (user == null || !password.equals(user.getPassword()))
            return null;

        return user;
    }

    public List<User> listAll() {
        return new ArrayList<>(Database.findAll());
    }
}
