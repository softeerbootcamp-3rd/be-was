package controller;

import annotation.PostMapping;
import dto.RequestDto;
import dto.ResponseDto;
import service.UserService;

import static constant.FilePath.MAIN_PAGE;

public class UserController {

    @PostMapping(path = "/user/create")
    public static ResponseDto create(RequestDto requestDto) {
        UserService.signUp(requestDto.getBody());

        return new ResponseDto().makeRedirect(MAIN_PAGE.getPath());
    }
}
