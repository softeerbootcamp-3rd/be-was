package controller;

import dto.RequestDto;
import exception.CustomException;
import service.UserService;
import webserver.ResponseHandler;

import java.io.DataOutputStream;

import static constant.FilePath.MAIN_PAGE;

public class UserController {

    public static void create(DataOutputStream dos, RequestDto requestDto) {

        try {
            UserService.signUp(requestDto.getParams());
        } catch (CustomException e) {
            ResponseHandler.sendError(dos, e.getErrorCode());
        }

        ResponseHandler.sendRedirect(dos, MAIN_PAGE.getPath());
    }
}
