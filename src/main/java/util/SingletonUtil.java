package util;

import controller.UserController;
import db.Database;
import service.UserService;

public class SingletonUtil {
    private static final UserController userController;
    private static final UserService userService;
    private static final Database database;

    static {
        userController = new UserController();
        userService = new UserService();
        database = new Database();
    }

    public static UserController getUserController() {
        return userController;
    }

    public static UserService getUserService() {
        return userService;
    }

    public static Database getDatabase() {
        return database;
    }
}
