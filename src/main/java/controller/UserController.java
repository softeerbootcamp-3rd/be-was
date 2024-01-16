package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import service.UserService;
import webserver.RequestHandler;

public class UserController {
    private static final Logger logger = LoggerFactory.getLogger(RequestHandler.class);
    private final UserService userService = new UserService();

    public void createUser(String userId, String password, String name, String email){
        logger.debug("createUser() 실행");

        userService.createUser(userId, password, name, email);
    }
}
