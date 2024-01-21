package controller;

import config.AppConfig;
import dto.HttpResponseDto;
import dto.UserSignUpDto;
import exception.BadRequestException;
import model.http.ContentType;
import model.http.Status;
import model.http.request.HttpRequest;
import service.UserService;

import java.util.Arrays;
import java.util.HashMap;

import static config.AppConfig.*;

public class UserControllerImpl implements UserController {
    private static class UserControllerHolder {
        public static final UserController INSTANCE = new UserControllerImpl(userService());
    }

    public static UserController getInstance() {
        return UserControllerHolder.INSTANCE;
    }

    private final UserService userService;

    public UserControllerImpl(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void doGet(HttpRequest httpRequest, HttpResponseDto httpResponseDto) {
        String pathUrl = httpRequest.getStartLine().getPathUrl();
        if (pathUrl.startsWith("/user/create")) {
            handleUserCreateRequest(httpResponseDto, pathUrl);
        }
    }

    private void handleUserCreateRequest(HttpResponseDto httpResponseDto, String pathUrl) {
        UserSignUpDto userSignUpDto = UserSignUpDto.fromUrlParameters(pathUrl);
        userService.signUp(userSignUpDto);
        httpResponseDto.setStatus(Status.REDIRECT);
        httpResponseDto.addHeader("Location", "/user/login.html");
    }
}
