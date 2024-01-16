package controller;

import service.UserService;

public class UserController {
    private final UserService userService = new UserService();

    public void createUser(String userId, String password, String name, String email){
        userService.createUser(userId, password, name, email);
    }
}
