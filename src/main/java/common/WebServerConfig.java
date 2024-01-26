package common;

import controller.UserController;
import service.UserService;

public class WebServerConfig {
    public static final UserController userController = new UserController();
    public static final UserService userService = new UserService();

    // file path
    public static final String INDEX_FILE_PATH = "/index.html";
    public static final String USER_CREATE_FORM_FAIL_FILE_PATH = "/user/form_fail.html";
    public static final String USER_CREATE_DUPLICATE_USERID_FAIL_FILE_PATH = "/user/form_userId_duplicate_fail.html";
    public static final String LOGIN_FAIL_FILE_PATH = "/user/login_failed.html";
    public static final String NOT_FOUND_FILE_PATH = "/not_found.html";
}
