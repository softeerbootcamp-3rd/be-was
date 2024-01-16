package controller;

import dto.UserCreateRequestDto;
import service.UserService;

public class UserController {
    private static final UserService userservice = new UserService();

    void create(UserCreateRequestDto userRequestDto){
        userservice.create(userRequestDto);
    }
}
