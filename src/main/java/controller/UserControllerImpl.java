package controller;

import dto.HttpResponseDto;
import dto.UserLoginDto;
import dto.UserSignUpDto;
import exception.InvalidLogin;
import model.http.Cookie;
import model.http.Status;
import model.http.request.HttpRequest;
import service.UserService;

import java.util.HashMap;
import java.util.UUID;

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
            handleUserCreateRequest(httpRequest, httpResponseDto);
        }
        if (pathUrl.startsWith("/user/login")) {
            handleUserLoginRequest(httpRequest, httpResponseDto);
        }
    }

    private void handleUserLoginRequest(HttpRequest httpRequest, HttpResponseDto httpResponseDto) {
        try {
            UUID sessionId = userService.login(UserLoginDto.fromRequestBody(httpRequest.getBody().getContent()));

            HashMap<String, String> map = new HashMap<>();
            map.put("Path", "/");

            Cookie cookie = new Cookie("sid", sessionId.toString(), map);
            httpResponseDto.addHeader("Set-Cookie", cookie.getCookieList());
            redirectToPath(httpResponseDto, "/index.html");
        } catch (InvalidLogin e) {
            redirectToPath(httpResponseDto, "/user/login_failed.html");
        }
    }
    private void handleUserCreateRequest(HttpRequest httpRequest, HttpResponseDto httpResponseDto) {
        UserSignUpDto userSignUpDto = UserSignUpDto.fromRequestBody(httpRequest.getBody().getContent());
        userService.signUp(userSignUpDto);
        redirectToPath(httpResponseDto, "/index.html");
    }
    private void redirectToPath(HttpResponseDto httpResponseDto, String path) {
        httpResponseDto.setStatus(Status.REDIRECT);
        httpResponseDto.addHeader("Location", path);
    }
}
