package common.config;

import controller.UserController;
import service.UserService;

public class WebServerConfig {
    public static final UserController userController = new UserController();
    public static final UserService userService = new UserService();
}
