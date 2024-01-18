package controller;

import dto.ResponseDto;
import dto.UserDto;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import service.UserService;

public class UserController {

    private static final UserService userService = new UserService();

    public ResponseDto route(String url) {
        if (url.startsWith("/user/create")) {
            return createUser(url);
        } else {
            return getPage(url);
        }
    }

    private ResponseDto getPage(String url) {
        String filePath = "src/main/resources/templates" + url;

        try {
            byte[] body = Files.readAllBytes(new File(filePath).toPath());
            return new ResponseDto(url, 200, body);
        } catch (IOException e) {
            byte[] body = "404 Not Found".getBytes();
            return new ResponseDto(url, 404, body);
        }
    }

    private ResponseDto createUser(String url) {
        String[] userData = url.split("\\?")[1].split("&");
        String userId = userData[0].split("=", -1)[1];
        String password = userData[1].split("=", -1)[1];
        String name = userData[2].split("=", -1)[1];
        String email = userData[3].split("=", -1)[1];

        UserDto userDto = new UserDto(userId, password, name, email);
        userService.saveUser(userDto);

        String location = "/";
        return new ResponseDto(location, 302, "".getBytes());
    }
}
