package service;

import controller.UserController;
import db.Database;
import model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserService {
    public static UserService getInstance() {
        return UserService.LazyHolder.INSTANCE;
    }

    private static class LazyHolder {
        private static final UserService INSTANCE = new UserService();
    }

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    public boolean saveUser(User user) {
        if (Database.findUserById(user.getUserId()) != null) {
            return false;
        }

        try {
            Database.addUser(user);
            logger.debug("user saved: {}", user);
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
