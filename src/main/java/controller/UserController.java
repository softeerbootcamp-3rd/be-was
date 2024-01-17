package controller;

import dto.UserCreateRequestDto;
import service.UserService;

public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    public void create(UserCreateRequestDto userRequestDto) {
        userService.create(userRequestDto);
    }
}