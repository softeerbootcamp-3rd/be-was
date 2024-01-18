package controller;

import model.User;
import validate.InputValidate;
import webserver.RequestParser;

import java.util.Map;

import static config.WebServerConfig.userService;

public class UserController {

    public String create(String userInfoQueryString) {
        InputValidate.validateUserInfo(userInfoQueryString);
        Map<String, String> userInfo = RequestParser.parseParameters(userInfoQueryString);
        User user = new User(
                userInfo.get("userId"),
                userInfo.get("password"),
                userInfo.get("name"),
                userInfo.get("email"));
        return userService.create(user);
    }
}
