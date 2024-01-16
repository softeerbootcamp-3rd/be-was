package util;

import controller.UserController;
import db.Database;
import service.UserService;

public class SingletonUtil {
    private static final SingletonUtil instance = new SingletonUtil();
    private final UserController userController;
    private final UserService userService;
    private final Database database;
    private SingletonUtil() {
        this.userController = new UserController();
        this.userService = new UserService();
        this.database = new Database();
    }

    public static UserController getUserController() {
        return instance.userController;
    }

    public static UserService getUserService() {
        return instance.userService;
    }

    public static Database getDatabase() {
        return instance.database;
    }
}
