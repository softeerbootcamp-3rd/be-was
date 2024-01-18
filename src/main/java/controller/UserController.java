package controller;

import dto.ResponseDto;
import dto.UserDto;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import service.UserService;
import utils.ParamBuilder;

public class UserController implements Controller {

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
        Map<String, String> params = ParamBuilder.getParams(url);

        try {
            userService.saveUser(params);
        } catch (NullPointerException e) {
            return new ResponseDto("", 400, "cannot find parameter.".getBytes());
        } catch (IllegalArgumentException e) {
            return new ResponseDto("", 400, "user id already exists.".getBytes());
        }

        String location = "/";
        return new ResponseDto(location, 302, "".getBytes());
    }
}
