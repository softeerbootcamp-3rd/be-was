package controller;

import annotation.GetMapping;
import dto.RequestDto;
import service.UserService;
import webserver.ResponseHandler;

import java.io.DataOutputStream;

import static constant.FilePath.MAIN_PAGE;

public class UserController {

    @GetMapping(path = "/user/create")
    public static void create(DataOutputStream dos, RequestDto requestDto) {
        UserService.signUp(requestDto.getParams());

        ResponseHandler.sendRedirect(dos, MAIN_PAGE.getPath());
    }
}
