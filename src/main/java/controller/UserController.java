package controller;

import dto.UserRequestDto;
import service.UserService;

public class UserController {
    private static final UserService userservice = new UserService();

    void create(UserRequestDto userRequestDto){
        userservice.create(userRequestDto);
    }
}
