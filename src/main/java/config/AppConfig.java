package config;

import controller.qna.QnaCreateController;
import controller.qna.QnaFormController;
import controller.user.UserCreateController;
import controller.user.UserListController;
import controller.user.UserLoginController;
import repository.QnaRepository;
import repository.UserRepository;
import service.QnaService;
import service.UserService;
import util.SessionManager;

public class AppConfig {
    private static UserCreateController userCreateController;
    private static UserLoginController userLoginController;
    private static UserListController userListController;
    private static QnaFormController qnaFormController;
    private static QnaCreateController qnaCreateController;
    private static UserService userService;
    private static QnaService qnaService;
    private static UserRepository userRepository;
    private static QnaRepository qnaRepository;
    private static SessionManager sessionManager;

    public static UserService userService() {
        if (userService == null) {
            userService = new UserService(userRepository());
        }
        return userService;
    }

    public static QnaService qnaService() {
        if (qnaService == null) {
            qnaService = new QnaService(qnaRepository());
        }
        return qnaService;
    }

    public static UserCreateController userCreateController() {
        if (userCreateController == null) {
            userCreateController = new UserCreateController(userService());
        }
        return userCreateController;
    }
    public static UserLoginController userLoginController() {
        if (userLoginController == null) {
            userLoginController = new UserLoginController(userService());
        }
        return userLoginController;
    }

    public static UserListController userListController() {
        if (userListController == null) {
            userListController = new UserListController(userService());
        }
        return userListController;
    }

    public static QnaFormController qnaFormController() {
        if (qnaFormController == null) {
            qnaFormController = new QnaFormController(userService());
        }
        return qnaFormController;
    }

    public static QnaCreateController qnaCreateController() {
        if (qnaCreateController == null) {
            qnaCreateController = new QnaCreateController(qnaService());
        }
        return qnaCreateController;
    }

    public static UserRepository userRepository() {
        if (userRepository == null) {
            userRepository = new UserRepository();
        }
        return userRepository;
    }

    public static QnaRepository qnaRepository() {
        if (qnaRepository == null) {
            qnaRepository = new QnaRepository();
        }
        return qnaRepository;
    }
}
