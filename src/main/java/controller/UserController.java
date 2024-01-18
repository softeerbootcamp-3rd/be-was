package controller;

import validate.InputValidate;
import webserver.RequestParser;

import java.util.Map;

import static config.WebServerConfig.userService;

public class UserController {

    public String create(String userInfoQueryString) {
        InputValidate.validateUserInfo(userInfoQueryString);
        Map<String, String> userInfo = RequestParser.parseParameters(userInfoQueryString);
        return userService.create(userInfo);
    }
}
