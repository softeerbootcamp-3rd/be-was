package controller;

import common.validate.InputValidate;
import dto.LoginRequest;
import model.User;
import session.SessionManager;

import static common.binder.Binder.bindQueryStringToObject;
import static webserver.WebServerConfig.userService;

public class UserController {

    public String create(String userInfo) throws Exception {
        InputValidate.validateUserInfo(userInfo);
        User user = bindQueryStringToObject(userInfo, User.class);
        return userService.create(user);
    }

    public String login(String loginInfo) throws Exception {
        LoginRequest loginRequest = bindQueryStringToObject(loginInfo, LoginRequest.class);
        User user = userService.login(loginRequest);
        return SessionManager.createSessionId(user);
    }
}
