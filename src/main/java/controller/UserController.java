package controller;

import dto.UserDto;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import service.UserService;
import utils.ResponseBuilder;

public class UserController {

    private static final UserService userService = new UserService();

    public void route(String url, OutputStream out) throws IOException {
        if (url.startsWith("/user/create")) {
            createUser(url, out);
        } else {
            getPage(url, out);
        }
    }

    private void getPage(String url, OutputStream out) throws IOException {
        DataOutputStream dos = new DataOutputStream(out);

        String filePath = "src/main/resources/templates" + url;

        ResponseBuilder.build200(dos, filePath);
    }

    private void createUser(String url, OutputStream out) throws IOException {
        String[] userData = url.split("\\?")[1].split("&");
        String userId = userData[0].split("=", -1)[1];
        String password = userData[1].split("=", -1)[1];
        String name = userData[2].split("=", -1)[1];
        String email = userData[3].split("=", -1)[1];

        UserDto userDto = new UserDto(userId, password, name, email);
        userService.saveUser(userDto);

        DataOutputStream dos = new DataOutputStream(out);

        url = "/";

        ResponseBuilder.build302(dos, url);
    }
}
