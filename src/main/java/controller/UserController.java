package controller;

import service.UserService;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class UserController {
    private final UserService userService = new UserService();

    public String signUp(Map<String, String> queryParams){
        if(!userService.signUp(queryParams)){
            return "/user/form.html";
        }
        return "/user/login.html";
    }

}
