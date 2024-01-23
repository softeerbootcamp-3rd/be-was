package controller;

import common.validate.InputValidate;
import model.User;

import static common.binder.Binder.bindQueryStringToObject;
import static common.config.WebServerConfig.userService;

public class UserController {

    public String create(String userInfo) throws Exception {
        InputValidate.validateUserInfo(userInfo);
        User user = bindQueryStringToObject(userInfo, User.class);
        return userService.create(user);
    }
}
