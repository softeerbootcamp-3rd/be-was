package controller;

import annotation.PostMapping;
import dto.RequestDto;
import service.UserService;
import webserver.ResponseHandler;

import java.io.DataOutputStream;

import static constant.FilePath.MAIN_PAGE;

public class UserController {

    @PostMapping(path = "/user/create")
    public static void create(DataOutputStream dos, RequestDto requestDto) {
        UserService.signUp(requestDto.getBody());

        ResponseHandler.sendRedirect(dos, MAIN_PAGE.getPath());
    }
}
