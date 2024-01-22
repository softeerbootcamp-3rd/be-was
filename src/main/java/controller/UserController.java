package controller;

import dto.ResourceDto;
import model.QueryParams;
import service.UserService;

public class UserController {
    private final UserService userService = new UserService();

    public ResourceDto process(QueryParams queryParams) {
        userService.createUser(queryParams);
        return ResourceDto.of("/index.html", 302);
    }

    public ResourceDto process(String path) {
        return ResourceDto.of(path);
    }
}
