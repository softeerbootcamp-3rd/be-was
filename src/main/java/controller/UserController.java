package controller;

import common.validate.InputValidate;
import model.User;

import static common.binder.Binder.bindQueryStringToObject;
import static common.config.WebServerConfig.userService;

public class UserController {

    public String create(String userInfoQueryString) throws Exception {
        InputValidate.validateUserInfo(userInfoQueryString);
        User user = bindQueryStringToObject(userInfoQueryString, User.class);
        return userService.create(user);
    }
}
