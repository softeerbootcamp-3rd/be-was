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

public class UserControllerImpl implements UserController {
    private static class UserControllerHolder {
        public static final UserController INSTANCE = new UserControllerImpl(AppConfig.userService());
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
            UserSignUpDto userSignUpDto = getPathParameter(pathUrl);
            userService.signUp(userSignUpDto);
            httpResponseDto.setStatus(Status.REDIRECT);
            httpResponseDto.addHeader("Location", "/user/login.html");
        }
    }

    private UserSignUpDto getPathParameter(String pathUrl) {
        try {
            HashMap<String, String> map = new HashMap<>();
            String[] splitUrl = pathUrl.split("\\?");
            String[] parameter = splitUrl[1].split("&");
            Arrays.stream(parameter).forEach(param -> {
                String[] value = param.split("=");
                map.put(value[0], value[1]);
            });
            return new UserSignUpDto(map.get("userId"), map.get("password"), map.get("name"), map.get("email"));
        } catch (IndexOutOfBoundsException | IllegalArgumentException e) {
            throw new BadRequestException("Please fill in all the necessary factors", e);
        }
    }
}
