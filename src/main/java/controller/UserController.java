package controller;

import model.QueryParams;
import service.UserService;

public class UserController {
    private final UserService userService = new UserService();

    public void process(QueryParams queryParams) {
        userService.createUser(queryParams);
    }
}
