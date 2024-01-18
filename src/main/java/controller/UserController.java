package controller;

import service.UserService;

import java.io.UnsupportedEncodingException;
import java.util.Map;

public class UserController {
    private final UserService userService = new UserService();

    public String signUp(Map<String, String> queryParams) throws UnsupportedEncodingException {
        if(!userService.signUp(queryParams)){
            return "/user/form.html";
        }
        return "/user/login.html";
    }

}
