package service;

import controller.UserController;
import db.Database;
import model.User;

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

    public boolean saveUser(User user) {
        if (Database.findUserById(user.getUserId()) != null) {
            return false;
        }

        try {
            Database.addUser(user);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public List<User> listAll() {
        return new ArrayList<>(Database.findAll());
    }
}
