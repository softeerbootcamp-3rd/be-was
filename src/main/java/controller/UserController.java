package controller;

import model.User;
import util.Util;

import java.net.URI;
import java.util.Map;

import static config.WebServerConfig.userService;

public class UserController {

    public String create(URI uri) {
        Map<String, String> parameters = Util.splitParamtersToKeyAndValue(uri.getQuery());
        User user = new User(
                parameters.get("userId"),
                parameters.get("password"),
                parameters.get("name"),
                parameters.get("email"));
        return userService.create(user);
    }
}
