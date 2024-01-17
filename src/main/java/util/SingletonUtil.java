package util;

import controller.HomeController;
import controller.UserController;
import service.UserService;

public class SingletonUtil {
    private static final HomeController homeController;
    private static final UserController userController;
    private static final UserService userService;

    static {
        homeController = new HomeController();
        userController = new UserController();
        userService = new UserService();
    }

    public static UserController getUserController() {
        return userController;
    }

    public static UserService getUserService() {
        return userService;
    }

    public static HomeController getHomeController() {
        return homeController;
    }
}
